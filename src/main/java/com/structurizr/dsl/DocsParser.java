package com.structurizr.dsl;

import com.structurizr.documentation.Documentable;
import com.structurizr.importer.documentation.DefaultImageImporter;
import com.structurizr.importer.documentation.DocumentationImporter;

import java.io.File;
import java.lang.reflect.Constructor;

final class DocsParser extends AbstractParser {

    private static final String DEFAULT_DOCUMENT_IMPORTER = "com.structurizr.importer.documentation.DefaultDocumentationImporter";

    private static final String GRAMMAR = "!docs <path> <fqn>";

    private static final int PATH_INDEX = 1;
    private static final int FQN_INDEX = 2;

    void parse(WorkspaceDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getWorkspace(), dslFile, tokens);
    }

    void parse(SoftwareSystemDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getSoftwareSystem(), dslFile, tokens);
    }

    void parse(ContainerDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getContainer(), dslFile, tokens);
    }

    void parse(ComponentDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getComponent(), dslFile, tokens);
    }

    private void parse(DslContext context, Documentable documentable, File dslFile, Tokens tokens) {
        // !docs <path>

        if (tokens.hasMoreThan(FQN_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(PATH_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String fullyQualifiedClassName = DEFAULT_DOCUMENT_IMPORTER;
        if (tokens.includes(FQN_INDEX)) {
            fullyQualifiedClassName = tokens.get(FQN_INDEX);
        }

        if (dslFile != null) {
            File path = new File(dslFile.getParentFile(), tokens.get(PATH_INDEX));

            if (!path.exists()) {
                throw new RuntimeException("Documentation path " + path + " does not exist");
            }

            if (!path.isDirectory()) {
                throw new RuntimeException("Documentation path " + path + " is not a directory");
            }

            try {
                Class documentationImporterClass = context.loadClass(fullyQualifiedClassName, dslFile);
                Constructor constructor = documentationImporterClass.getDeclaredConstructor();
                DocumentationImporter documentationImporter = (DocumentationImporter)constructor.newInstance();
                documentationImporter.importDocumentation(documentable, path);

                if (!tokens.includes(FQN_INDEX)) {
                    DefaultImageImporter imageImporter = new DefaultImageImporter();
                    imageImporter.importDocumentation(documentable, path);
                }
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Error importing documentation from " + path.getAbsolutePath() + ": " + fullyQualifiedClassName + " was not found");
            } catch (Exception e) {
                throw new RuntimeException("Error importing documentation from " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

}