package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ElementNotPermittedInViewException;
import com.structurizr.view.StaticView;

import java.util.Set;

final class StaticViewContentParser extends ViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;
    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 2;

    private static final String WILDCARD = "*";
    private static final String RELATIONSHIP = "->";

    void parseInclude(StaticViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier|expression> [identifier|expression...] or include <*|identifier> -> <*|identifier>");
        }

        StaticView view = context.getView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // include <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                context.getView().add(relationship);
            }
        } else if (tokens.contains(WILDCARD)) {
            // include *
            view.addDefaultElements();
        } else {
            // include <identifier|expression> [identifier|expression...]
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String token = tokens.get(i);

                if (isElementExpression(token)) {
                    new StaticViewExpressionParser().parseElementExpression(token, context).forEach(e -> addElementToView(e, view, null));
                } else if (isRelationshipExpression(token)) {
                    new StaticViewExpressionParser().parseRelationshipExpression(token, context).forEach(r -> addRelationshipToView(r, view));
                } else {
                    // assume the token is an identifier
                    Element element = context.getElement(token);
                    Relationship relationship = context.getRelationship(token);
                    if (element == null && relationship == null) {
                        throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                    }

                    if (element != null) {
                        if (element instanceof ElementGroup) {
                            ElementGroup group = (ElementGroup)element;
                            for (Element e : group.getElements()) {
                                addElementToView(e, view, null);
                            }
                        } else {
                            addElementToView(element, view, token);
                        }
                    }

                    if (relationship != null) {
                        addRelationshipToView(relationship, view);
                    }
                }
            }
        }
    }

    void parseExclude(StaticViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier|expression> [identifier|expression...] or exclude <*|identifier> -> <*|identifier>");
        }

        StaticView view = context.getView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // exclude <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                context.getView().remove(relationship);
            }
        } else {
            // exclude <identifier|expression> [identifier|expression...]
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String token = tokens.get(i);

                if (isElementExpression(token)) {
                    new StaticViewExpressionParser().parseElementExpression(token, context).forEach(e -> removeElementFromView(e, view));
                } else if (isRelationshipExpression(token)) {
                    new StaticViewExpressionParser().parseRelationshipExpression(token, context).forEach(r -> removeRelationshipFromView(r, view));
                } else {
                    // assume the token is an identifier
                    Element element = context.getElement(token);
                    Relationship relationship = context.getRelationship(token);
                    if (element == null && relationship == null) {
                        throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                    }

                    if (element != null) {
                        removeElementFromView(element, view);
                    }

                    if (relationship != null) {
                        removeRelationshipFromView(relationship, view);
                    }
                }
            }
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

    private void removeRelationshipFromView(Relationship relationship, StaticView view) {
        view.remove(relationship);
    }

}