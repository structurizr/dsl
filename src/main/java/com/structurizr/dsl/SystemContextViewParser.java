package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;

final class SystemContextViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "systemContext <software system identifier> [key] [description] {";

    private static final String VIEW_TYPE = "SystemContext";

    private static final int SOFTWARE_SYSTEM_IDENTIFIER_INDEX = 1;
    private static final int KEY_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;

    SystemContextView parse(DslContext context, Tokens tokens) {
        // systemContext <software system identifier> [key] [description] {

        Workspace workspace = context.getWorkspace();
        SoftwareSystem softwareSystem;
        String key = "";
        String description = "";

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(SOFTWARE_SYSTEM_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String softwareSystemIdentifier = tokens.get(SOFTWARE_SYSTEM_IDENTIFIER_INDEX);
        Element element = context.getElement(softwareSystemIdentifier);
        if (element == null) {
            throw new RuntimeException("The software system \"" + softwareSystemIdentifier + "\" does not exist");
        }
        if (element instanceof SoftwareSystem) {
            softwareSystem = (SoftwareSystem)element;
        } else {
            throw new RuntimeException("The element \"" + softwareSystemIdentifier + "\" is not a software system");
        }

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = workspace.getViews().generateViewKey(VIEW_TYPE);
        }
        validateViewKey(key);

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, key, description);
        view.setEnterpriseBoundaryVisible(true);

        return view;
    }

}