package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.*;

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

                if (token.startsWith(ELEMENT_TAG_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(ELEMENT_TAG_EQUALS_EXPRESSION.length()).split(",");
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof StaticStructureElement).forEach(element -> {
                        if (hasAllTags(element, tags)) {
                            addElementToView(element, context.getView());
                        }
                    });
                } else if (token.startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof StaticStructureElement).forEach(element -> {
                        if (!hasAllTags(element, tags)) {
                            addElementToView(element, context.getView());
                        }
                    });
                } else if (token.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(RELATIONSHIP_TAG_EQUALS_EXPRESSION.length()).split(",");
                    context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                        if (view.isElementInView(relationship.getSource()) && view.isElementInView(relationship.getDestination())) {
                            if (hasAllTags(relationship, tags)) {
                                view.add(relationship);
                            }
                        }
                    });
                } else if (token.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
                    context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                        if (view.isElementInView(relationship.getSource()) && view.isElementInView(relationship.getDestination())) {
                            if (!hasAllTags(relationship, tags)) {
                                view.add(relationship);
                            }
                        }
                    });
                } else {
                    // assume the token is an identifier
                    Element element = context.getElement(token);
                    Relationship relationship = context.getRelationship(token);
                    if (element == null && relationship == null) {
                        throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                    }

                    if (element != null) {
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
                            throw new RuntimeException("The element \"" + token + "\" can not be added to this type of view");
                        }
                    }

                    if (relationship != null) {
                        view.add(relationship);
                    }
                }
            }
        }
    }

    private void addElementToView(Element element, StaticView view) {
        try {
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
            }
        } catch (ElementNotPermittedInViewException e) {
            // ignore
        }
    }

    private void removeElementFromView(Element element, StaticView view) {
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
        }
    }

    private boolean hasAllTags(ModelItem modelItem, String[] tags) {
        boolean result = true;

        for (String tag : tags) {
            result = result && modelItem.hasTag(tag.trim());
        }

        return result;
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

                if (token.startsWith(ELEMENT_TAG_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(ELEMENT_TAG_EQUALS_EXPRESSION.length()).split(",");
                    view.getElements().stream().map(ElementView::getElement).forEach(element -> {
                        if (hasAllTags(element, tags)) {
                            removeElementFromView(element, context.getView());
                        }
                    });
                } else if (token.startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
                    view.getElements().stream().map(ElementView::getElement).forEach(element -> {
                        if (!hasAllTags(element, tags)) {
                            removeElementFromView(element, context.getView());
                        }
                    });
                } else if (token.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(RELATIONSHIP_TAG_EQUALS_EXPRESSION.length()).split(",");
                    view.getRelationships().stream().map(RelationshipView::getRelationship).forEach(relationship -> {
                        if (hasAllTags(relationship, tags)) {
                            view.remove(relationship);
                        }
                    });
                } else if (token.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION)) {
                    String[] tags = token.substring(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
                    view.getRelationships().stream().map(RelationshipView::getRelationship).forEach(relationship -> {
                        if (!hasAllTags(relationship, tags)) {
                            view.remove(relationship);
                        }
                    });
                } else {
                    // assume the token is an identifier
                    Element element = context.getElement(token);
                    Relationship relationship = context.getRelationship(token);
                    if (element == null && relationship == null) {
                        throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                    }

                    if (element != null) {
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
                        } else {
                            throw new RuntimeException("The element \"" + token + "\" can not be added to this view");
                        }
                    }

                    if (relationship != null) {
                        view.remove(relationship);
                    }
                }
            }
        }
    }

}