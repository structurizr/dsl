package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.*;

final class StaticViewContentParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    private static final String WILDCARD = "*";

    void parseInclude(StaticViewDslContext context, Tokens tokens) {
        // include <*|identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [identifier...]");
        }

        StaticView view = context.getView();

        if (tokens.contains(WILDCARD)) {
            view.addDefaultElements();
        } else {
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String identifier = tokens.get(i);

                Element element = context.getElement(identifier);
                Relationship relationship = context.getRelationship(identifier);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof Person) {
                        view.add((Person) element);
                    } else if (element instanceof SoftwareSystem) {
                        view.add((SoftwareSystem) element);
                    } else if (element instanceof Container && (view instanceof ContainerView)) {
                        ((ContainerView) view).add((Container) element);
                    } else if (element instanceof Container && (view instanceof ComponentView)) {
                        ((ComponentView) view).add((Container) element);
                    } else if (element instanceof Component && (view instanceof ComponentView)) {
                        ((ComponentView) view).add((Component) element);
                    } else {
                        throw new RuntimeException("The element \"" + identifier + "\" can not be added to this type of view");
                    }
                }

                if (relationship != null) {
                    view.add(relationship);
                }
            }
        }

    }

    void parseExclude(StaticViewDslContext context, Tokens tokens) {
        // exclude <identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...]");
        }

        StaticView view = context.getView();

        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String identifier = tokens.get(i);

            Element element = context.getElement(identifier);
            Relationship relationship = context.getRelationship(identifier);
            if (element == null && relationship == null) {
                throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
            }

            if (element != null) {
                if (element instanceof Person) {
                    view.remove((Person) element);
                } else if (element instanceof SoftwareSystem) {
                    view.remove((SoftwareSystem) element);
                } else if (element instanceof Container && (view instanceof ContainerView)) {
                    ((ContainerView) view).remove((Container) element);
                } else if (element instanceof Container && (view instanceof ComponentView)) {
                    ((ComponentView) view).remove((Container) element);
                } else if (element instanceof Component && (view instanceof ComponentView)) {
                    ((ComponentView) view).remove((Component) element);
                } else {
                    throw new RuntimeException("The element \"" + identifier + "\" can not be added to this view");
                }
            }

            if (relationship != null) {
                view.remove(relationship);
            }
        }
    }

}