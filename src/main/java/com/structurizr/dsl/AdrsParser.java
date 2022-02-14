package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.documentation.AdrToolsImporter;
import com.structurizr.documentation.AutomaticDocumentationTemplate;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;

final class AdrsParser extends AbstractParser {

    private static final String GRAMMAR = "!adrs <path>";

    private static final int PATH_INDEX = 1;

    void parse(WorkspaceDslContext context, File file, Tokens tokens) {
        parse(context.getWorkspace(), null, file, tokens);
    }

    void parse(SoftwareSystemDslContext context, File file, Tokens tokens) {
        parse(context.getWorkspace(), context.getSoftwareSystem(), file, tokens);
    }

    private void parse(Workspace workspace, SoftwareSystem softwareSystem, File file, Tokens tokens) {
        // !adrs <path>

        if (tokens.hasMoreThan(PATH_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(PATH_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (file != null) {
            File path = new File(file.getParentFile(), tokens.get(PATH_INDEX));

            if (!path.exists()) {
                throw new RuntimeException("Documentation path " + path + " does not exist");
            }

            if (!path.isDirectory()) {
                throw new RuntimeException("Documentation path " + path + " is not a directory");
            }

            AdrToolsImporter adrToolsImporter = new AdrToolsImporter(workspace, path);
            try {
                if (softwareSystem == null) {
                    adrToolsImporter.importArchitectureDecisionRecords();
                } else {
                    adrToolsImporter.importArchitectureDecisionRecords(softwareSystem);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error importing ADRs from " + path.getAbsolutePath() + ": " + e.getMessage());
            }

            try {
                AutomaticDocumentationTemplate template = new AutomaticDocumentationTemplate(workspace);
                template.addImages(path);
            } catch (Exception e) {
                throw new RuntimeException("Error importing images from " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

}