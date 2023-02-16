package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;
import com.structurizr.view.CustomView;
import com.structurizr.view.ElementNotPermittedInViewException;

final class CustomViewContentParser extends ModelViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    void parseInclude(CustomViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [*|identifier...]");
        }

        CustomView view = context.getCustomView();

        // include <identifier> [identifier...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.equals(WILDCARD) || token.equals(ELEMENT_WILDCARD)) {
                // include * or include element==*
                view.addDefaultElements();
            } else if (isExpression(token)) {
                new CustomViewExpressionParser().parseExpression(token, context).forEach(mi -> addModelItemToView(mi, view, null));
            } else {
                new CustomViewExpressionParser().parseIdentifier(token, context).forEach(mi -> addModelItemToView(mi, view, token));
            }
        }
    }

    void parseExclude(CustomViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...]");
        }

        CustomView view = context.getCustomView();

        // exclude <identifier> [identifier...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (isExpression(token)) {
                new CustomViewExpressionParser().parseExpression(token, context).forEach(mi -> removeModelItemFromView(mi, view));
            } else {
                new CustomViewExpressionParser().parseIdentifier(token, context).forEach(mi -> removeModelItemFromView(mi, view));
            }
        }
    }

    private void addModelItemToView(ModelItem modelItem, CustomView view, String identifier) {
        if (modelItem instanceof Element) {
            addElementToView((Element)modelItem, view, identifier);
        } else {
            addRelationshipToView((Relationship)modelItem, view);
        }
    }

    private void addElementToView(Element element, CustomView view, String identifier) {
        try {
            if (element instanceof CustomElement) {
                view.add((CustomElement) element);
            } else {
                if (!StringUtils.isNullOrEmpty(identifier)) {
                    throw new RuntimeException("The element \"" + identifier + "\" can not be added to this type of view");
                }
            }
        } catch (ElementNotPermittedInViewException e) {
            // ignore
        }
    }

    private void removeModelItemFromView(ModelItem modelItem, CustomView view) {
        if (modelItem instanceof Element) {
            removeElementFromView((Element)modelItem, view);
        } else {
            removeRelationshipFromView((Relationship)modelItem, view);
        }
    }

    private void removeElementFromView(Element element, CustomView view) {
        if (element instanceof CustomElement) {
            view.remove((CustomElement) element);
        }
    }

    private void addRelationshipToView(Relationship relationship, CustomView view) {
        if (view.isElementInView(relationship.getSource()) && view.isElementInView(relationship.getDestination())) {
            view.add(relationship);
        }
    }

}