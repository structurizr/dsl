package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.view.ComponentView;

final class ComponentViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "component <container identifier> [key] [description] {";

    private static final String VIEW_TYPE = "Component";

    private static final int CONTAINER_IDENTIFIER_INDEX = 1;
    private static final int KEY_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;

    ComponentView parse(DslContext context, Tokens tokens) {
        // component <container identifier> [key] [description] {

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(CONTAINER_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Workspace workspace = context.getWorkspace();
        Container container;
        String key = "";
        String description = "";

        String containerIdentifier = tokens.get(CONTAINER_IDENTIFIER_INDEX);
        Element element = context.getElement(containerIdentifier);
        if (element == null) {
            throw new RuntimeException("The container \"" + containerIdentifier + "\" does not exist");
        }
        if (element instanceof Container) {
            container = (Container)element;
        } else {
            throw new RuntimeException("The element \"" + containerIdentifier + "\" is not a container");
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

        ComponentView view = workspace.getViews().createComponentView(container, key, description);
        view.setExternalSoftwareSystemBoundariesVisible(true);

        return view;
    }

}