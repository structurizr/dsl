package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;

class DynamicViewParser extends AbstractParser {

    private static final String VIEW_TYPE = "Dynamic";

    private static final int SCOPE_IDENTIFIER_INDEX = 1;
    private static final int KEY_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;

    private static final String WILDCARD = "*";

    DynamicView parse(DslContext context, Tokens tokens) {
        // dynamic <*|software system identifier|container identifier> [key] [description] {

        Workspace workspace = context.getWorkspace();
        String key = "";
        String description = "";

        if (!tokens.includes(SCOPE_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: dynamic <*|software system identifier|container identifier> [key] [description] {");
        }

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = generateViewKey(workspace, VIEW_TYPE);
        }
        validateViewKey(key);

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        DynamicView view;

        String scopeIdentifier = tokens.get(SCOPE_IDENTIFIER_INDEX);
        if (WILDCARD.equals(scopeIdentifier)) {
            view = workspace.getViews().createDynamicView(key, description);
        } else {
            Element element = context.getElement(scopeIdentifier);
            if (element == null) {
                throw new RuntimeException("The software system or container \"" + scopeIdentifier + "\" does not exist");
            }

            if (element instanceof SoftwareSystem) {
                view = workspace.getViews().createDynamicView((SoftwareSystem)element, key, description);
            } else if (element instanceof Container) {
                view = workspace.getViews().createDynamicView((Container)element, key, description);
            } else {
                throw new RuntimeException("The element \"" + scopeIdentifier + "\" is not a software system or container");
            }
        }

        return view;
    }

}