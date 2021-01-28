package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.documentation.AutomaticDocumentationTemplate;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;

final class DocsParser extends AbstractParser {

    private static final int PATH_INDEX = 1;

    void parse(WorkspaceDslContext context, File file, Tokens tokens) {
        parse(context.getWorkspace(), null, file, tokens);
    }

    void parse(SoftwareSystemDslContext context, File file, Tokens tokens) {
        parse(context.getWorkspace(), context.getSoftwareSystem(), file, tokens);
    }

    private void parse(Workspace workspace, SoftwareSystem softwareSystem, File file, Tokens tokens) {
        // !docs <path>

        if (!tokens.includes(PATH_INDEX)) {
            throw new RuntimeException("Expected: !docs <path>");
        }

        if (file != null) {
            File path = new File(file.getParentFile(), tokens.get(PATH_INDEX));

            if (!path.exists()) {
                throw new RuntimeException("Documentation path " + path + " does not exist");
            }

            if (!path.isDirectory()) {
                throw new RuntimeException("Documentation path " + path + " is not a directory");
            }

            AutomaticDocumentationTemplate template = new AutomaticDocumentationTemplate(workspace);
            template.setRecursive(true);
            try {
                if (softwareSystem == null) {
                    template.addSections(path);
                } else {
                    template.addSections(softwareSystem, path);
                }
                template.addImages(path);
            } catch (IOException e) {
                throw new RuntimeException("Error importing documentation from " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

}