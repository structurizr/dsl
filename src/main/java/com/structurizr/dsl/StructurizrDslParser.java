package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main DSL parser class - forms the API for using the parser.
 */
public final class StructurizrDslParser extends StructurizrDslTokens {

    private static final String BOM = "\uFEFF";

    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*");

    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*?(//|#).*$");
    private static final String MULTI_LINE_COMMENT_START_TOKEN = "/*";
    private static final String MULTI_LINE_COMMENT_END_TOKEN = "*/";
    private static final String MULTI_LINE_SEPARATOR = "\\";

    private static final Pattern STRING_SUBSTITUTION_PATTERN = Pattern.compile("(\\$\\{[a-zA-Z0-9-_.]+?})");

    private static final String STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME = "structurizr.dsl.identifier";

    private Charset characterEncoding = StandardCharsets.UTF_8;
    private IdentifierScope identifierScope = IdentifierScope.Flat;
    private Stack<DslContext> contextStack;
    private Set<String> parsedTokens = new HashSet<>();
    private IdentifiersRegister identifiersRegister;
    private Map<String, Constant> constants;

    private List<String> dslSourceLines = new ArrayList<>();
    private Workspace workspace;
    private boolean extendingWorkspace = false;

    private boolean restricted = false;

    /**
     * Creates a new instance of the parser.
     */
    public StructurizrDslParser() {
        contextStack = new Stack<>();
        identifiersRegister = new IdentifiersRegister();
        constants = new HashMap<>();
    }

    /**
     * Provides a way to change the character encoding used by the DSL parser.
     *
     * @param characterEncoding     a Charset instance
     */
    public void setCharacterEncoding(Charset characterEncoding) {
        if (characterEncoding == null) {
            throw new IllegalArgumentException("A character encoding must be specified");
        }

        this.characterEncoding = characterEncoding;
    }

    IdentifierScope getIdentifierScope() {
        return identifierScope;
    }

    void setIdentifierScope(IdentifierScope identifierScope) {
        if (identifierScope == null) {
            identifierScope = IdentifierScope.Flat;
        }

        this.identifierScope = identifierScope;
        this.identifiersRegister.setIdentifierScope(identifierScope);
    }

    /**
     * Sets whether to run this parser in restricted mode (this stops !include, !docs, !adrs from working).
     *
     * @param restricted        true for restricted mode, false otherwise
     */
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    /**
     * Gets the workspace that has been created by parsing the Structurizr DSL.
     *
     * @return  a Workspace instance
     */
    public Workspace getWorkspace() {
        if (workspace != null) {
            DslUtils.setDsl(workspace, getParsedDsl());
        }

        return workspace;
    }

    private String getParsedDsl() {
        StringBuilder buf = new StringBuilder();

        for (String line : dslSourceLines) {
            buf.append(line);
            buf.append(System.lineSeparator());
        }

        return buf.toString();
    }

    void parse(DslParserContext context, File path) throws StructurizrDslParserException {
        parse(path);

        context.copyFrom(identifiersRegister);
    }

    /**
     * Parses the specified Structurizr DSL file(s), adding the parsed content to the workspace.
     * If "path" represents a single file, that single file will be parsed.
     * If "path" represents a directory, all files in that directory (recursively) will be parsed.
     *
     * @param path      a File object representing a file or directory
     * @throws StructurizrDslParserException when something goes wrong
     */
    public void parse(File path) throws StructurizrDslParserException {
        if (path == null) {
            throw new StructurizrDslParserException("A file must be specified");
        }

        if (!path.exists()) {
            throw new StructurizrDslParserException("The file at " + path.getAbsolutePath() + " does not exist");
        }

        List<File> files = FileUtils.findFiles(path);
        try {
            for (File file : files) {
                parse(Files.readAllLines(file.toPath(), characterEncoding), file);
            }
        } catch (IOException e) {
            throw new StructurizrDslParserException(e.getMessage());
        }
    }

    void parse(DslParserContext context, String dsl) throws StructurizrDslParserException {
        parse(dsl);

        context.copyFrom(identifiersRegister);
    }

    /**
     * Parses the specified Structurizr DSL fragment, adding the parsed content to the workspace.
     *
     * @param dsl       a DSL fragment
     * @throws StructurizrDslParserException when something goes wrong
     */
    public void parse(String dsl) throws StructurizrDslParserException {
        if (StringUtils.isNullOrEmpty(dsl)) {
            throw new StructurizrDslParserException("A DSL fragment must be specified");
        }

        List<String> lines = Arrays.asList(dsl.split("\\r?\\n"));
        parse(lines, new File("."));
    }

    private List<DslLine> preProcessLines(List<String> lines) {
        List<DslLine> dslLines = new ArrayList<>();

        int lineNumber = 1;
        StringBuilder buf = new StringBuilder();
        boolean lineComplete = true;

        for (String line : lines) {
            if (line.endsWith(MULTI_LINE_SEPARATOR)) {
                buf.append(line, 0, line.length()-1);
                lineComplete = false;
            } else {
                if (lineComplete) {
                    buf.append(line);
                } else {
                    buf.append(line.stripLeading());
                    lineComplete = true;
                }
            }

            if (lineComplete) {
                dslLines.add(new DslLine(buf.toString(), lineNumber));
                buf = new StringBuilder();
            }

            lineNumber++;
        }

        return dslLines;
    }

    void parse(List<String> lines, File dslFile) throws StructurizrDslParserException {
        List<DslLine> dslLines = preProcessLines(lines);

        for (DslLine dslLine : dslLines) {
            boolean includeInDslSourceLines = true;

            String line = dslLine.getSource();

            if (line.startsWith(BOM)) {
                // this caters for files encoded as "UTF-8 with BOM"
                line = line.substring(1);
            }

            try {
                if (EMPTY_LINE_PATTERN.matcher(line).matches()) {
                    // do nothing
                } else if (COMMENT_PATTERN.matcher(line).matches()) {
                    // do nothing
                } else if (inContext(InlineScriptDslContext.class)) {
                    if (DslContext.CONTEXT_END_TOKEN.equals(line.trim())) {
                        endContext();
                    } else {
                        getContext(InlineScriptDslContext.class).addLine(line);
                    }
                } else {
                    List<String> listOfTokens = new Tokenizer().tokenize(line);
                    listOfTokens = listOfTokens.stream().map(this::substituteStrings).collect(Collectors.toList());

                    Tokens tokens = new Tokens(listOfTokens);

                    String identifier = null;
                    if (tokens.size() > 3 && ASSIGNMENT_OPERATOR_TOKEN.equals(tokens.get(1))) {
                        identifier = tokens.get(0);
                        identifiersRegister.validateIdentifierName(identifier);

                        tokens = new Tokens(listOfTokens.subList(2, listOfTokens.size()));
                    }

                    String firstToken = tokens.get(0);

                    if (line.trim().startsWith(MULTI_LINE_COMMENT_START_TOKEN) && line.trim().endsWith(MULTI_LINE_COMMENT_END_TOKEN)) {
                        // do nothing
                    } else if (firstToken.startsWith(MULTI_LINE_COMMENT_START_TOKEN)) {
                        startContext(new CommentDslContext());

                    } else if (inContext(CommentDslContext.class) && line.trim().endsWith(MULTI_LINE_COMMENT_END_TOKEN)) {
                        endContext();

                    } else if (inContext(CommentDslContext.class)) {
                        // do nothing

                    } else if (DslContext.CONTEXT_END_TOKEN.equals(tokens.get(0))) {
                        endContext();

                    } else if (INCLUDE_FILE_TOKEN.equalsIgnoreCase(firstToken)) {
                        if (!restricted || tokens.get(1).startsWith("https://")) {
                            String leadingSpace = line.substring(0, line.indexOf(INCLUDE_FILE_TOKEN));

                            IncludedDslContext context = new IncludedDslContext(dslFile);
                            new IncludeParser().parse(context, tokens);
                            for (IncludedFile includedFile : context.getFiles()) {
                                List<String> paddedLines = new ArrayList<>();
                                for (String unpaddedLine : includedFile.getLines()) {
                                    paddedLines.add(leadingSpace + unpaddedLine);
                                }

                                parse(paddedLines, includedFile.getFile());
                            }

                            includeInDslSourceLines = false;
                        }

                    } else if (PLUGIN_TOKEN.equalsIgnoreCase(firstToken)) {
                        if (!restricted) {
                            String fullyQualifiedClassName = new PluginParser().parse(getContext(), tokens.withoutContextStartToken());
                            startContext(new PluginDslContext(fullyQualifiedClassName, dslFile, this));
                            if (!shouldStartContext(tokens)) {
                                // run the plugin immediately, without looking for parameters
                                endContext();
                            }
                        }

                    } else if (inContext(PluginDslContext.class)) {
                        new PluginParser().parseParameter(getContext(PluginDslContext.class), tokens);

                    } else if (SCRIPT_TOKEN.equalsIgnoreCase(firstToken)) {
                        if (!restricted) {
                            ScriptParser scriptParser = new ScriptParser();
                            if (scriptParser.isInlineScript(tokens)) {
                                String language = scriptParser.parseInline(tokens.withoutContextStartToken());
                                startContext(new InlineScriptDslContext(getContext(), dslFile, language));
                            } else {
                                String filename = scriptParser.parseExternal(tokens.withoutContextStartToken());
                                startContext(new ExternalScriptDslContext(getContext(), dslFile, filename));

                                if (shouldStartContext(tokens)) {
                                    // we'll wait for parameters before executing the script
                                } else {
                                    endContext();
                                }
                            }
                        }

                    } else if (inContext(ExternalScriptDslContext.class)) {
                        new ScriptParser().parseParameter(getContext(ExternalScriptDslContext.class), tokens);

                    } else if (tokens.size() > 2 && RELATIONSHIP_TOKEN.equals(tokens.get(1)) && (inContext(ModelDslContext.class) || inContext(EnterpriseDslContext.class) || inContext(CustomElementDslContext.class) || inContext(PersonDslContext.class) || inContext(SoftwareSystemDslContext.class) || inContext(ContainerDslContext.class) || inContext(ComponentDslContext.class) || inContext(DeploymentEnvironmentDslContext.class) || inContext(DeploymentNodeDslContext.class) || inContext(InfrastructureNodeDslContext.class) || inContext(SoftwareSystemInstanceDslContext.class) || inContext(ContainerInstanceDslContext.class))) {
                        Relationship relationship = new ExplicitRelationshipParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipDslContext(relationship));
                        }

                        registerIdentifier(identifier, relationship);

                    } else if (tokens.size() >= 2 && RELATIONSHIP_TOKEN.equals(tokens.get(0)) && (inContext(CustomElementDslContext.class) || inContext(PersonDslContext.class) || inContext(SoftwareSystemDslContext.class) || inContext(ContainerDslContext.class) || inContext(ComponentDslContext.class) || inContext(DeploymentNodeDslContext.class) || inContext(InfrastructureNodeDslContext.class) || inContext(SoftwareSystemInstanceDslContext.class) || inContext(ContainerInstanceDslContext.class))) {
                        Relationship relationship = new ImplicitRelationshipParser().parse(getContext(ModelItemDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipDslContext(relationship));
                        }

                        registerIdentifier(identifier, relationship);

                    } else if ((REF_TOKEN.equalsIgnoreCase(firstToken) || EXTEND_TOKEN.equalsIgnoreCase(firstToken)) && (inContext(ModelDslContext.class))) {
                        ModelItem modelItem = new RefParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            if (modelItem instanceof Person) {
                                startContext(new PersonDslContext((Person)modelItem));
                            } else if (modelItem instanceof SoftwareSystem) {
                                startContext(new SoftwareSystemDslContext((SoftwareSystem)modelItem));
                            } else if (modelItem instanceof Container) {
                                startContext(new ContainerDslContext((Container) modelItem));
                            } else if (modelItem instanceof Component) {
                                startContext(new ComponentDslContext((Component)modelItem));
                            } else if (modelItem instanceof DeploymentEnvironment) {
                                startContext(new DeploymentEnvironmentDslContext(((DeploymentEnvironment)modelItem).getName()));
                            } else if (modelItem instanceof DeploymentNode) {
                                startContext(new DeploymentNodeDslContext((DeploymentNode)modelItem));
                            } else if (modelItem instanceof InfrastructureNode) {
                                startContext(new InfrastructureNodeDslContext((InfrastructureNode)modelItem));
                            } else if (modelItem instanceof SoftwareSystemInstance) {
                                startContext(new SoftwareSystemInstanceDslContext((SoftwareSystemInstance)modelItem));
                            } else if (modelItem instanceof ContainerInstance) {
                                startContext(new ContainerInstanceDslContext((ContainerInstance)modelItem));
                            } else if (modelItem instanceof Relationship) {
                                startContext(new RelationshipDslContext((Relationship)modelItem));
                            }
                        }

                        if (!StringUtils.isNullOrEmpty(identifier)) {
                            if (modelItem instanceof Element) {
                                registerIdentifier(identifier, (Element)modelItem);
                            } else if (modelItem instanceof Relationship) {
                                registerIdentifier(identifier, (Relationship)modelItem);
                            }
                        }

                    } else if (CUSTOM_ELEMENT_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ModelDslContext.class))) {
                        CustomElement customElement = new CustomElementParser().parse(getContext(GroupableDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new CustomElementDslContext(customElement));
                        }

                        registerIdentifier(identifier, customElement);

                    } else if (PERSON_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ModelDslContext.class) || inContext(EnterpriseDslContext.class))) {
                        Person person = new PersonParser().parse(getContext(GroupableDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new PersonDslContext(person));
                        }

                        registerIdentifier(identifier, person);

                    } else if (SOFTWARE_SYSTEM_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ModelDslContext.class) || inContext(EnterpriseDslContext.class))) {
                        SoftwareSystem softwareSystem = new SoftwareSystemParser().parse(getContext(GroupableDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new SoftwareSystemDslContext(softwareSystem));
                        }

                        registerIdentifier(identifier, softwareSystem);

                    } else if (CONTAINER_TOKEN.equalsIgnoreCase(firstToken) && inContext(SoftwareSystemDslContext.class)) {
                        Container container = new ContainerParser().parse(getContext(SoftwareSystemDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new ContainerDslContext(container));
                        }

                        registerIdentifier(identifier, container);

                    } else if (COMPONENT_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        Component component = new ComponentParser().parse(getContext(ContainerDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new ComponentDslContext(component));
                        }

                        registerIdentifier(identifier, component);

                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(ModelDslContext.class), tokens);

                        startContext(new ModelDslContext(group));
                        registerIdentifier(identifier, group);
                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(EnterpriseDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(EnterpriseDslContext.class), tokens);

                        startContext(new EnterpriseDslContext(group));
                        registerIdentifier(identifier, group);
                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(SoftwareSystemDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(SoftwareSystemDslContext.class), tokens);

                        SoftwareSystem softwareSystem = getContext(SoftwareSystemDslContext.class).getSoftwareSystem();
                        group.setParent(softwareSystem);
                        startContext(new SoftwareSystemDslContext(softwareSystem, group));
                        registerIdentifier(identifier, group);
                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(ContainerDslContext.class), tokens);

                        Container container = getContext(ContainerDslContext.class).getContainer();
                        group.setParent(container);
                        startContext(new ContainerDslContext(container, group));
                        registerIdentifier(identifier, group);
                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentEnvironmentDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(DeploymentEnvironmentDslContext.class), tokens);

                        String environment = getContext(DeploymentEnvironmentDslContext.class).getEnvironment();
                        startContext(new DeploymentEnvironmentDslContext(environment, group));
                        registerIdentifier(identifier, group);
                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        ElementGroup group = new GroupParser().parse(getContext(DeploymentNodeDslContext.class), tokens);

                        DeploymentNode deploymentNode = getContext(DeploymentNodeDslContext.class).getDeploymentNode();
                        startContext(new DeploymentNodeDslContext(deploymentNode, group));
                        registerIdentifier(identifier, group);
                    } else if (TAGS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !getContext(ModelItemDslContext.class).hasGroup()) {
                        new ModelItemParser().parseTags(getContext(ModelItemDslContext.class), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && getContext(ModelItemDslContext.class).getModelItem() instanceof Element && !getContext(ModelItemDslContext.class).hasGroup()) {
                        new ModelItemParser().parseDescription(getContext(ModelItemDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class) && !getContext(ContainerDslContext.class).hasGroup()) {
                        new ContainerParser().parseTechnology(getContext(ContainerDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class) && !getContext(ComponentDslContext.class).hasGroup()) {
                        new ComponentParser().parseTechnology(getContext(ComponentDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        new DeploymentNodeParser().parseTechnology(getContext(DeploymentNodeDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(InfrastructureNodeDslContext.class)) {
                        new InfrastructureNodeParser().parseTechnology(getContext(InfrastructureNodeDslContext.class), tokens);

                    } else if (INSTANCES_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        new DeploymentNodeParser().parseInstances(getContext(DeploymentNodeDslContext.class), tokens);

                    } else if (URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !getContext(ModelItemDslContext.class).hasGroup()) {
                        new ModelItemParser().parseUrl(getContext(ModelItemDslContext.class), tokens);

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace.getModel()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class) && !getContext(ModelItemDslContext.class).hasGroup()) {
                        startContext(new PropertiesDslContext(getContext(ConfigurationDslContext.class).getWorkspace()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !getContext(ModelItemDslContext.class).hasGroup()) {
                        startContext(new PropertiesDslContext(getContext(ModelItemDslContext.class).getModelItem()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace.getViews().getConfiguration()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext(ViewDslContext.class).getView()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext((ElementStyleDslContext.class)).getStyle()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext((RelationshipStyleDslContext.class)).getStyle()));

                    } else if (inContext(PropertiesDslContext.class)) {
                        new PropertyParser().parse(getContext(PropertiesDslContext.class), tokens);

                    } else if (PERSPECTIVES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !getContext(ModelItemDslContext.class).hasGroup()) {
                        startContext(new ModelItemPerspectivesDslContext(getContext(ModelItemDslContext.class).getModelItem()));

                    } else if (inContext(ModelItemPerspectivesDslContext.class)) {
                        new ModelItemParser().parsePerspective(getContext(ModelItemPerspectivesDslContext.class), tokens);

                    } else if (WORKSPACE_TOKEN.equalsIgnoreCase(firstToken) && contextStack.empty()) {
                        if (parsedTokens.contains(WORKSPACE_TOKEN)) {
                            throw new RuntimeException("Multiple workspaces are not permitted in a DSL definition");
                        }
                        DslParserContext dslParserContext = new DslParserContext(dslFile, restricted);
                        dslParserContext.setIdentifierRegister(identifiersRegister);

                        workspace = new WorkspaceParser().parse(dslParserContext, tokens.withoutContextStartToken());
                        extendingWorkspace = !workspace.getModel().isEmpty();
                        startContext(new WorkspaceDslContext());
                        parsedTokens.add(WORKSPACE_TOKEN);
                    } else if (IMPLIED_RELATIONSHIPS_TOKEN.equalsIgnoreCase(firstToken) || IMPLIED_RELATIONSHIPS_TOKEN.substring(1).equalsIgnoreCase(firstToken)) {
                        new ImpliedRelationshipsParser().parse(getContext(), tokens);

                    } else if (NAME_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        new WorkspaceParser().parseName(getContext(), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        new WorkspaceParser().parseDescription(getContext(), tokens);

                    } else if (MODEL_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (parsedTokens.contains(MODEL_TOKEN)) {
                            throw new RuntimeException("Multiple models are not permitted in a DSL definition");
                        }

                        startContext(new ModelDslContext());
                        parsedTokens.add(MODEL_TOKEN);

                    } else if (VIEWS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (parsedTokens.contains(VIEWS_TOKEN)) {
                            throw new RuntimeException("Multiple view sets are not permitted in a DSL definition");
                        }

                        startContext(new ViewsDslContext());
                        parsedTokens.add(VIEWS_TOKEN);

                    } else if (BRANDING_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new BrandingDslContext(dslFile));

                    } else if (BRANDING_LOGO_TOKEN.equalsIgnoreCase(firstToken) && inContext(BrandingDslContext.class)) {
                        new BrandingParser().parseLogo(getContext(BrandingDslContext.class), tokens, restricted);

                    } else if (BRANDING_FONT_TOKEN.equalsIgnoreCase(firstToken) && inContext(BrandingDslContext.class)) {
                        new BrandingParser().parseFont(getContext(BrandingDslContext.class), tokens);

                    } else if (STYLES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new StylesDslContext());

                    } else if (ELEMENT_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        ElementStyle elementStyle = new ElementStyleParser().parseElementStyle(getContext(), tokens.withoutContextStartToken());
                        startContext(new ElementStyleDslContext(elementStyle, dslFile));

                    } else if (ELEMENT_STYLE_BACKGROUND_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseBackground(getContext(ElementStyleDslContext.class), tokens);

                    } else if ((ELEMENT_STYLE_COLOUR_TOKEN.equalsIgnoreCase(firstToken) || ELEMENT_STYLE_COLOR_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseColour(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_STROKE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseStroke(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_STROKE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseStrokeWidth(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_SHAPE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseShape(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_BORDER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseBorder(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_OPACITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseOpacity(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseWidth(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_HEIGHT_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseHeight(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_FONT_SIZE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseFontSize(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_METADATA_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseMetadata(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseDescription(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_ICON_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseIcon(getContext(ElementStyleDslContext.class), tokens, restricted);

                    } else if (RELATIONSHIP_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        RelationshipStyle relationshipStyle = new RelationshipStyleParser().parseRelationshipStyle(getContext(), tokens.withoutContextStartToken());
                        startContext(new RelationshipStyleDslContext(relationshipStyle));

                    } else if (RELATIONSHIP_STYLE_THICKNESS_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseThickness(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if ((RELATIONSHIP_STYLE_COLOUR_TOKEN.equalsIgnoreCase(firstToken) || RELATIONSHIP_STYLE_COLOR_TOKEN.equalsIgnoreCase(firstToken)) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseColour(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_DASHED_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseDashed(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_OPACITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseOpacity(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseWidth(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_FONT_SIZE_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseFontSize(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_POSITION_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parsePosition(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_LINE_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseLineStyle(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_ROUTING_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseRouting(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (ENTERPRISE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        new EnterpriseParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new EnterpriseDslContext());

                    } else if (DEPLOYMENT_ENVIRONMENT_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        String environment = new DeploymentEnvironmentParser().parse(tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentEnvironmentDslContext(environment));
                        }

                        registerIdentifier(identifier, new DeploymentEnvironment(environment));

                    } else if (DEPLOYMENT_GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentEnvironmentDslContext.class)) {
                        String group = new DeploymentGroupParser().parse(tokens.withoutContextStartToken());

                        registerIdentifier(identifier, new DeploymentGroup(group));

                    } else if (DEPLOYMENT_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentEnvironmentDslContext.class)) {
                        DeploymentNode deploymentNode = new DeploymentNodeParser().parse(getContext(DeploymentEnvironmentDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentNodeDslContext(deploymentNode));
                        }

                        registerIdentifier(identifier, deploymentNode);
                    } else if (DEPLOYMENT_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        DeploymentNode deploymentNode = new DeploymentNodeParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentNodeDslContext(deploymentNode));
                        }

                        registerIdentifier(identifier, deploymentNode);
                    } else if (INFRASTRUCTURE_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        InfrastructureNode infrastructureNode = new InfrastructureNodeParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new InfrastructureNodeDslContext(infrastructureNode));
                        }

                        registerIdentifier(identifier, infrastructureNode);

                    } else if (SOFTWARE_SYSTEM_INSTANCE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        SoftwareSystemInstance softwareSystemInstance = new SoftwareSystemInstanceParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new SoftwareSystemInstanceDslContext(softwareSystemInstance));
                        }

                        registerIdentifier(identifier, softwareSystemInstance);

                    } else if (CONTAINER_INSTANCE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        ContainerInstance containerInstance = new ContainerInstanceParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new ContainerInstanceDslContext(containerInstance));
                        }

                        registerIdentifier(identifier, containerInstance);

                    } else if (HEALTH_CHECK_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticStructureElementInstanceDslContext.class)) {
                        new HealthCheckParser().parse(getContext(StaticStructureElementInstanceDslContext.class), tokens.withoutContextStartToken());
                    } else if (CUSTOM_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        CustomView view = new CustomViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new CustomViewDslContext(view));

                    } else if (SYSTEM_LANDSCAPE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        SystemLandscapeView view = new SystemLandscapeViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new SystemLandscapeViewDslContext(view));

                    } else if (SYSTEM_CONTEXT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        SystemContextView view = new SystemContextViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new SystemContextViewDslContext(view));

                    } else if (CONTAINER_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ContainerView view = new ContainerViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ContainerViewDslContext(view));

                    } else if (COMPONENT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ComponentView view = new ComponentViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ComponentViewDslContext(view));

                    } else if (DYNAMIC_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        DynamicView view = new DynamicViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new DynamicViewDslContext(view));

                    } else if (DEPLOYMENT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        DeploymentView view = new DeploymentViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new DeploymentViewDslContext(view));

                    } else if (FILTERED_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        FilteredView view = new FilteredViewParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new FilteredViewDslContext(view));
                        }

                    } else if (IMAGE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ImageView view = new ImageViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ImageViewDslContext(view));

                    } else if (DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(firstToken) && inContext(DynamicViewDslContext.class)) {
                        startContext(new DynamicViewParallelSequenceDslContext(getContext(DynamicViewDslContext.class)));

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewContentParser().parseInclude(getContext(CustomViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewContentParser().parseExclude(getContext(CustomViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewAnimationStepParser().parse(getContext(CustomViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        startContext(new CustomViewAnimationDslContext(getContext(CustomViewDslContext.class).getCustomView()));

                    } else if (inContext(CustomViewAnimationDslContext.class)) {
                        new CustomViewAnimationStepParser().parse(getContext(CustomViewAnimationDslContext.class), tokens);

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewContentParser().parseInclude(getContext(StaticViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewContentParser().parseExclude(getContext(StaticViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewAnimationStepParser().parse(getContext(StaticViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        startContext(new StaticViewAnimationDslContext(getContext(StaticViewDslContext.class).getView()));

                    } else if (inContext(StaticViewAnimationDslContext.class)) {
                        new StaticViewAnimationStepParser().parse(getContext(StaticViewAnimationDslContext.class), tokens);

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewContentParser().parseInclude(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewContentParser().parseExclude(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewAnimationStepParser().parse(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        startContext(new DeploymentViewAnimationDslContext(getContext(DeploymentViewDslContext.class).getView()));

                    } else if (inContext(DeploymentViewAnimationDslContext.class)) {
                        new DeploymentViewAnimationStepParser().parse(getContext(DeploymentViewAnimationDslContext.class), tokens);

                    } else if (AUTOLAYOUT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new AutoLayoutParser().parse(getContext(ModelViewDslContext.class), tokens);

                    } else if (DEFAULT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new DefaultViewParser().parse(getContext(ViewDslContext.class));

                    } else if (VIEW_TITLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new ViewParser().parseTitle(getContext(ViewDslContext.class), tokens);

                    } else if (VIEW_DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new ViewParser().parseDescription(getContext(ViewDslContext.class), tokens);

                    } else if (PLANTUML_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser(restricted).parsePlantUML(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (MERMAID_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser(restricted).parseMermaid(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (KROKI_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser(restricted).parseKroki(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (IMAGE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser(restricted).parseImage(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (inContext(DynamicViewDslContext.class)) {
                        RelationshipView relationshipView = new DynamicViewContentParser().parseRelationship(getContext(DynamicViewDslContext.class), tokens);

                        if (inContext(DynamicViewParallelSequenceDslContext.class)) {
                            getContext(DynamicViewParallelSequenceDslContext.class).hasRelationships(true);
                        }

                        if (shouldStartContext(tokens)) {
                            startContext(new DynamicViewRelationshipContext(relationshipView));
                        }

                    } else if (URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(DynamicViewRelationshipContext.class)) {
                        new DynamicViewRelationshipParser().parseUrl(getContext(DynamicViewRelationshipContext.class), tokens.withoutContextStartToken());

                    } else if (THEME_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ViewsDslContext.class) || inContext(StylesDslContext.class))) {
                        new ThemeParser().parseTheme(getContext(), tokens);

                    } else if (THEMES_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ViewsDslContext.class) || inContext(StylesDslContext.class))) {
                        new ThemeParser().parseThemes(getContext(), tokens);

                    } else if (TERMINOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new TerminologyDslContext());

                    } else if (ENTERPRISE_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseEnterprise(getContext(), tokens);

                    } else if (PERSON_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parsePerson(getContext(), tokens);

                    } else if (SOFTWARE_SYSTEM_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseSoftwareSystem(getContext(), tokens);

                    } else if (CONTAINER_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseContainer(getContext(), tokens);

                    } else if (COMPONENT_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseComponent(getContext(), tokens);

                    } else if (DEPLOYMENT_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseDeploymentNode(getContext(), tokens);

                    } else if (INFRASTRUCTURE_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseInfrastructureNode(getContext(), tokens);

                    } else if (TERMINOLOGY_RELATIONSHIP_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseRelationship(getContext(), tokens);

                    } else if (CONFIGURATION_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        startContext(new ConfigurationDslContext());

                    } else if (SCOPE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        new ConfigurationParser().parseScope(getContext(), tokens);

                    } else if (VISIBILITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        new ConfigurationParser().parseVisibility(getContext(), tokens);

                    } else if (USERS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        startContext(new UsersDslContext());

                    } else if (inContext(UsersDslContext.class)) {
                        new UserRoleParser().parse(getContext(), tokens);

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (!restricted) {
                            new DocsParser().parse(getContext(WorkspaceDslContext.class), dslFile, tokens);
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(SoftwareSystemDslContext.class)) {
                        if (!restricted) {
                            new DocsParser().parse(getContext(SoftwareSystemDslContext.class), dslFile, tokens);
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        if (!restricted) {
                            new DocsParser().parse(getContext(ContainerDslContext.class), dslFile, tokens);
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class)) {
                        if (!restricted) {
                            new DocsParser().parse(getContext(ComponentDslContext.class), dslFile, tokens);
                        }

                    } else if (ADRS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (!restricted) {
                            new AdrsParser().parse(getContext(WorkspaceDslContext.class), dslFile, tokens);
                        }

                    } else if (ADRS_TOKEN.equalsIgnoreCase(firstToken) && inContext(SoftwareSystemDslContext.class)) {
                        if (!restricted) {
                            new AdrsParser().parse(getContext(SoftwareSystemDslContext.class), dslFile, tokens);
                        }

                    } else if (ADRS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        if (!restricted) {
                            new AdrsParser().parse(getContext(ContainerDslContext.class), dslFile, tokens);
                        }

                    } else if (ADRS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class)) {
                        if (!restricted) {
                            new AdrsParser().parse(getContext(ComponentDslContext.class), dslFile, tokens);
                        }

                    } else if (CONSTANT_TOKEN.equalsIgnoreCase(firstToken)) {
                        Constant constant = new ConstantParser().parse(getContext(), tokens);
                        constants.put(constant.getName(), constant);

                    } else if (IDENTIFIERS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        setIdentifierScope(new IdentifierScopeParser().parse(getContext(), tokens));

                    } else {
                        String[] expectedTokens;
                        if (getContext() == null) {
                            if (getWorkspace() == null) {
                                // the workspace hasn't yet been created
                                expectedTokens = new String[]{
                                        StructurizrDslTokens.WORKSPACE_TOKEN
                                };
                            } else {
                                expectedTokens = new String[0];
                            }
                        } else {
                            expectedTokens = getContext().getPermittedTokens();
                        }

                        if (expectedTokens.length > 0) {
                            StringBuilder buf = new StringBuilder();
                            for (String expectedToken : expectedTokens) {
                                buf.append(expectedToken);
                                buf.append(", ");
                            }
                            throw new StructurizrDslParserException("Unexpected tokens (expected: " + buf.substring(0, buf.length() - 2) + ")");
                        } else {
                            throw new StructurizrDslParserException("Unexpected tokens");
                        }
                    }
                }

                if (includeInDslSourceLines) {
                    dslSourceLines.add(line);
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    throw new StructurizrDslParserException(e.getMessage(), dslFile, dslLine.getLineNumber(), line);
                } else {
                    throw new StructurizrDslParserException(e.getClass().getSimpleName(), dslFile, dslLine.getLineNumber(), line);
                }
            }
        }
    }

    private String substituteStrings(String token) {
        Matcher m = STRING_SUBSTITUTION_PATTERN.matcher(token);
        while (m.find()) {
            String before = m.group(0);
            String after = null;
            String name = before.substring(2, before.length()-1);
            if (constants.containsKey(name)) {
                after = constants.get(name).getValue();
            } else {
                if (!restricted) {
                    String environmentVariable = System.getenv().get(name);
                    if (environmentVariable != null) {
                        after = environmentVariable;
                    }
                }
            }

            if (after != null) {
                token = token.replace(before, after);
            }
        }

        return token;
    }

    private boolean shouldStartContext(Tokens tokens) {
        return DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(tokens.get(tokens.size()-1));
    }

    private void startContext(DslContext context) {
        context.setWorkspace(workspace);
        context.setIdentifierRegister(identifiersRegister);
        context.setExtendingWorkspace(extendingWorkspace);
        contextStack.push(context);
    }

    private DslContext getContext() {
        if (!contextStack.empty()) {
            return contextStack.peek();
        } else {
            return null;
        }
    }

    private <T> T getContext(Class<T> clazz) throws StructurizrDslParserException {
        if (inContext(clazz)) {
            return (T)contextStack.peek();
        } else {
            throw new StructurizrDslParserException("Expected " + clazz.getName() + " but got " + contextStack.peek().getClass().getName());
        }
    }

    private void endContext() throws StructurizrDslParserException {
        if (!contextStack.empty()) {
            DslContext context = contextStack.pop();
            context.end();
        } else {
            throw new StructurizrDslParserException("Unexpected end of context");
        }
    }

    /**
     * Gets the identifier register in use (this is the mapping of DSL identifiers to elements/relationships).
     *
     * @return      an IdentifiersRegister object
     */
    public IdentifiersRegister getIdentifiersRegister() {
        return identifiersRegister;
    }

    private void registerIdentifier(String identifier, Element element) {
        identifiersRegister.register(identifier, element);
        element.addProperty(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME, identifiersRegister.findIdentifier(element));
    }

    private void registerIdentifier(String identifier, Relationship relationship) {
        identifiersRegister.register(identifier, relationship);
        relationship.addProperty(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME, identifiersRegister.findIdentifier(relationship));
    }

    private boolean inContext(Class clazz) {
        if (contextStack.empty()) {
            return false;
        }

        return clazz.isAssignableFrom(contextStack.peek().getClass());
    }

}