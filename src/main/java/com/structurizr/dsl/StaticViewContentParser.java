package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class StaticViewContentParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;
    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 2;

    private static final String WILDCARD = "*";
    private static final String RELATIONSHIP = "->";

    void parseInclude(StaticViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>");
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
            // include <identifier> [identifier...]
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
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>");
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
            // exclude <identifier> [identifier...]
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

    private Set<Relationship> findRelationships(StaticViewDslContext context, String sourceElementIdentifier, String destinationElementIdentifier) {
        Set<Element> sourceElements = new HashSet<>();
        Set<Element> destinationElements = new HashSet<>();
        Set<Relationship> relationships = new HashSet<>();

        if (sourceElementIdentifier.equals(WILDCARD)) {
            sourceElements.addAll(context.getView().getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element sourceElement = context.getElement(sourceElementIdentifier);
            if (sourceElement == null) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist");
            }

            if (!context.getView().isElementInView(sourceElement)) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist in the view");
            }

            sourceElements.add(sourceElement);
        }

        if (destinationElementIdentifier.equals(WILDCARD)) {
            destinationElements.addAll(context.getView().getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element destinationElement = context.getElement(destinationElementIdentifier);
            if (destinationElement == null) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist");
            }

            if (!context.getView().isElementInView(destinationElement)) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist in the view");
            }

            destinationElements.add(destinationElement);
        }

        for (Element sourceElement : sourceElements) {
            for (Relationship relationship : sourceElement.getRelationships()) {
                if (destinationElements.contains(relationship.getDestination())) {
                    relationships.add(relationship);
                }
            }
        }

        return relationships;
    }

}