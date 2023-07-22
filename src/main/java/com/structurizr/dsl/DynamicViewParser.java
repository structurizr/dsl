package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;

import java.text.DecimalFormat;

class DynamicViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "dynamic <*|software system identifier|container identifier> [key] [description] {";

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

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(SCOPE_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        DynamicView view;

        String scopeIdentifier = tokens.get(SCOPE_IDENTIFIER_INDEX);
        if (WILDCARD.equals(scopeIdentifier)) {
            if (tokens.includes(KEY_INDEX)) {
                key = tokens.get(KEY_INDEX);
            } else {
                key = workspace.getViews().generateViewKey(VIEW_TYPE);
            }
            validateViewKey(key);

            view = workspace.getViews().createDynamicView(key, description);
        } else {
            Element element = context.getElement(scopeIdentifier);
            if (element == null) {
                throw new RuntimeException("The software system or container \"" + scopeIdentifier + "\" does not exist");
            }

            if (element instanceof SoftwareSystem) {
                if (tokens.includes(KEY_INDEX)) {
                    key = tokens.get(KEY_INDEX);
                } else {
                    key = workspace.getViews().generateViewKey(VIEW_TYPE);
                }
                validateViewKey(key);

                view = workspace.getViews().createDynamicView((SoftwareSystem)element, key, description);
            } else if (element instanceof Container) {
                Container container = (Container)element;
                if (tokens.includes(KEY_INDEX)) {
                    key = tokens.get(KEY_INDEX);
                } else {
                    key = workspace.getViews().generateViewKey(VIEW_TYPE);
                }
                validateViewKey(key);

                view = workspace.getViews().createDynamicView((Container)element, key, description);
            } else {
                throw new RuntimeException("The element \"" + scopeIdentifier + "\" is not a software system or container");
            }
        }

        view.setExternalBoundariesVisible(true);

        return view;
    }

}