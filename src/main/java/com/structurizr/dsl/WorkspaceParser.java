package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.WorkspaceUtils;

import java.io.File;

final class WorkspaceParser extends AbstractParser {

    private static final String GRAMMAR_STANDALONE = "workspace [name] [description]";
    private static final String GRAMMAR_EXTENDS = "workspace extends <file|url>";

    private static final String STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME = "structurizr.dsl.identifier";

    private static final int FIRST_INDEX = 1;
    private static final int SECOND_INDEX = 2;

    Workspace parse(DslParserContext context, Tokens tokens) {
        // workspace [name] [description]
        // workspace extends <file|url>

        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        if (tokens.hasMoreThan(SECOND_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR_STANDALONE + " or " + GRAMMAR_EXTENDS);
        }

        if (tokens.includes(FIRST_INDEX)) {
            String firstToken = tokens.get(FIRST_INDEX);

            if (StructurizrDslTokens.EXTENDS_TOKEN.equals(firstToken)) {
                if (tokens.includes(SECOND_INDEX)) {
                    String source = tokens.get(SECOND_INDEX);

                    try {
                        if (source.startsWith("https://")) {
                            if (source.endsWith(".json")) {
                                String json = readFromUrl(source);
                                workspace = WorkspaceUtils.fromJson(json);
                                registerIdentifiers(workspace, context);
                            } else {
                                String dsl = readFromUrl(source);
                                StructurizrDslParser structurizrDslParser = new StructurizrDslParser();
                                structurizrDslParser.parse(context, dsl);
                                workspace = structurizrDslParser.getWorkspace();
                            }
                        } else {
                            if (context.isRestricted()) {
                                throw new RuntimeException("Cannot import workspace from a file when running in restricted mode");
                            }

                            if (context.getFile() != null) {
                                File file = new File(context.getFile().getParent(), source);
                                if (!file.exists()) {
                                    throw new RuntimeException(file.getCanonicalPath() + " could not be found");
                                }

                                if (file.isDirectory()) {
                                    throw new RuntimeException(file.getCanonicalPath() + " should be a single file");
                                }

                                if (source.endsWith(".json")) {
                                    workspace = WorkspaceUtils.loadWorkspaceFromJson(file);
                                    registerIdentifiers(workspace, context);
                                } else {
                                    StructurizrDslParser structurizrDslParser = new StructurizrDslParser();
                                    structurizrDslParser.parse(context, file);
                                    workspace = structurizrDslParser.getWorkspace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    throw new RuntimeException("Expected: " + GRAMMAR_EXTENDS);
                }
            } else {
                workspace.setName(firstToken);

                if (tokens.includes(SECOND_INDEX)) {
                    workspace.setDescription(tokens.get(SECOND_INDEX));
                }
            }
        }

        return workspace;
    }

    private void registerIdentifiers(Workspace workspace, DslParserContext context) {
        for (Element element : workspace.getModel().getElements()) {
            if (element.getProperties().containsKey(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME)) {
                String identifier = element.getProperties().get(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME);
                context.identifiersRegister.register(identifier, element);
            }
        }

        for (Relationship relationship : workspace.getModel().getRelationships()) {
            if (relationship.getProperties().containsKey(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME)) {
                String identifier = relationship.getProperties().get(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME);
                context.identifiersRegister.register(identifier, relationship);
            }
        }
    }

    void parseName(DslContext context, Tokens tokens) {
        // name <name>
        if (tokens.hasMoreThan(FIRST_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: name <name>");
        }

        if (!tokens.includes(FIRST_INDEX)) {
            throw new RuntimeException("Expected: name <name>");
        }

        String name = tokens.get(FIRST_INDEX);
        context.getWorkspace().setName(name);
    }

    void parseDescription(DslContext context, Tokens tokens) {
        // description <description>
        if (tokens.hasMoreThan(FIRST_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <description>");
        }

        if (!tokens.includes(FIRST_INDEX)) {
            throw new RuntimeException("Expected: description <description>");
        }

        String description = tokens.get(FIRST_INDEX);
        context.getWorkspace().setDescription(description);
    }

}