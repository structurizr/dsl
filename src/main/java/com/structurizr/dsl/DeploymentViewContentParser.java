package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.ElementView;
import com.structurizr.view.RelationshipView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class DeploymentViewContentParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;
    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 2;

    private static final String WILDCARD = "*";
    private static final String RELATIONSHIP = "->";

    void parseInclude(DeploymentViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>");
        }

        DeploymentView view = context.getView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // include <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                view.add(relationship);
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
                    if (element instanceof CustomElement) {
                        view.add((CustomElement)element);
                    } else if (element instanceof DeploymentNode) {
                        DeploymentNode deploymentNode = (DeploymentNode) element;
                        if (deploymentNode.getEnvironment().equals(view.getEnvironment())) {
                            view.add(deploymentNode);
                        }
                    } else {
                        throw new RuntimeException("The element \"" + identifier + "\" can not be added to this view (it is not a deployment node)");
                    }
                }

                if (relationship != null) {
                    view.add(relationship);
                }
            }
        }
    }

    void parseExclude(DeploymentViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>");
        }

        DeploymentView view = context.getView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // exclude <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                view.remove(relationship);
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
                    if (element instanceof CustomElement) {
                        view.remove((CustomElement) element);
                    } else if (element instanceof DeploymentNode) {
                        view.remove((DeploymentNode)element);
                    } else if (element instanceof InfrastructureNode) {
                        view.remove((InfrastructureNode)element);
                    } else if (element instanceof ContainerInstance) {
                        view.remove((ContainerInstance)element);
                    } else {
                        throw new RuntimeException("The element \"" + identifier + "\" can not be added to this view");
                    }
                }

                if (relationship != null) {
                    // remove the specified relationship
                    view.remove(relationship);

                    // and also remove any replicated versions of the specified relationship
                    Collection<Relationship> replicatedRelationshipsInView = view.getRelationships().stream().map(RelationshipView::getRelationship).filter(r -> r.getLinkedRelationshipId() != null && r.getLinkedRelationshipId().equals(relationship.getId())).collect(Collectors.toSet());
                    for (Relationship r : replicatedRelationshipsInView) {
                        view.remove(r);
                    }
                }
            }
        }
    }

    private Set<Relationship> findRelationships(DeploymentViewDslContext context, String sourceElementIdentifier, String destinationElementIdentifier) {
        DeploymentView view = context.getView();
        Set<Element> sourceElements = new HashSet<>();
        Set<Element> destinationElements = new HashSet<>();
        Set<Relationship> relationships = new HashSet<>();

        if (sourceElementIdentifier.equals(WILDCARD)) {
            sourceElements.addAll(view.getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element sourceElement = context.getElement(sourceElementIdentifier);
            if (sourceElement == null) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist");
            }

            if (view.isElementInView(sourceElement)) {
                sourceElements.add(sourceElement);
            } else {
                // the element itself might not be in the view, but perhaps an "instance" is
                if (sourceElement instanceof SoftwareSystem || sourceElement instanceof Container) {
                    sourceElements.addAll(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof StaticStructureElementInstance).map(e -> (StaticStructureElementInstance)e).filter(e -> e.getElement() == sourceElement).collect(Collectors.toSet()));
                }
            }

            if (sourceElements.isEmpty()) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist in the view");
            }
        }

        if (destinationElementIdentifier.equals(WILDCARD)) {
            destinationElements.addAll(view.getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element destinationElement = context.getElement(destinationElementIdentifier);
            if (destinationElement == null) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist");
            }

            if (view.isElementInView(destinationElement)) {
                destinationElements.add(destinationElement);
            } else {
                // the element itself might not be in the view, but perhaps an "instance" is
                if (destinationElement instanceof SoftwareSystem || destinationElement instanceof Container) {
                    destinationElements.addAll(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof StaticStructureElementInstance).map(e -> (StaticStructureElementInstance)e).filter(e -> e.getElement() == destinationElement).collect(Collectors.toSet()));
                }
            }

            if (destinationElements.isEmpty()) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist in the view");
            }
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