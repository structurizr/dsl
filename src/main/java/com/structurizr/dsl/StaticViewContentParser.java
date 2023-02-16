package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ElementNotPermittedInViewException;
import com.structurizr.view.StaticView;

final class StaticViewContentParser extends ModelViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    void parseInclude(StaticViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier|expression> [*|identifier|expression...]");
        }

        StaticView view = context.getView();

        // include <identifier|expression> [identifier|expression...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.equals(WILDCARD) || token.equals(ELEMENT_WILDCARD)) {
                // include * or include element==*
                view.addDefaultElements();
            } else if (isExpression(token)) {
                new StaticViewExpressionParser().parseExpression(token, context).forEach(mi -> addModelItemToView(mi, view, null));
            } else {
                new StaticViewExpressionParser().parseIdentifier(token, context).forEach(mi -> addModelItemToView(mi, view, token));
            }
        }
    }

    void parseExclude(StaticViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier|expression> [identifier|expression...]");
        }

        StaticView view = context.getView();

        // exclude <identifier|expression> [identifier|expression...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (isExpression(token)) {
                new StaticViewExpressionParser().parseExpression(token, context).forEach(mi -> removeModelItemFromView(mi, view));
            } else {
                new StaticViewExpressionParser().parseIdentifier(token, context).forEach(mi -> removeModelItemFromView(mi, view));
            }
        }
    }

    private void addModelItemToView(ModelItem modelItem, StaticView view, String identifier) {
        if (modelItem instanceof Element) {
            addElementToView((Element)modelItem, view, identifier);
        } else {
            addRelationshipToView((Relationship)modelItem, view);
        }
    }

    private void addElementToView(Element element, StaticView view, String identifier) {
        try {
            if (element instanceof CustomElement) {
                view.add((CustomElement) element);
            } else if (element instanceof Person) {
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
                if (!StringUtils.isNullOrEmpty(identifier)) {
                    throw new RuntimeException("The element \"" + identifier + "\" can not be added to this type of view");
                }
            }
        } catch (ElementNotPermittedInViewException e) {
            // ignore
        }
    }

    private void removeModelItemFromView(ModelItem modelItem, StaticView view) {
        if (modelItem instanceof Element) {
            removeElementFromView((Element)modelItem, view);
        } else {
            removeRelationshipFromView((Relationship)modelItem, view);
        }
    }

    private void removeElementFromView(Element element, StaticView view) {
        if (element instanceof CustomElement) {
            view.remove((CustomElement) element);
        } else if (element instanceof Person) {
            view.remove((Person) element);
        } else if (element instanceof SoftwareSystem) {
            view.remove((SoftwareSystem) element);
        } else if (element instanceof Container && (view instanceof ContainerView)) {
            ((ContainerView) view).remove((Container) element);
        } else if (element instanceof Container && (view instanceof ComponentView)) {
            ((ComponentView) view).remove((Container) element);
        } else if (element instanceof Component && (view instanceof ComponentView)) {
            ((ComponentView) view).remove((Component) element);
        }
    }

    private void addRelationshipToView(Relationship relationship, StaticView view) {
        if (view.isElementInView(relationship.getSource()) && view.isElementInView(relationship.getDestination())) {
            view.add(relationship);
        }
    }

}