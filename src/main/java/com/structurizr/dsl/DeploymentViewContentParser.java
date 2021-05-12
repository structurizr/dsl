package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class DeploymentViewContentParser extends ViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;
    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 2;

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
                String token = tokens.get(i);

                if (isElementExpression(token)) {
                    new DeploymentViewExpressionParser().parseElementExpression(token, context).forEach(e -> addElementToView(e, view, null));
                } else if (isRelationshipExpression(token)) {
                    new DeploymentViewExpressionParser().parseRelationshipExpression(token, context).forEach(r -> addRelationshipToView(r, view));
                } else {
                    // assume the token is an identifier
                    Element element = context.getElement(token);
                    Relationship relationship = context.getRelationship(token);
                    if (element == null && relationship == null) {
                        throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                    }

                    if (element != null) {
                        addElementToView(element, view, token);
                    }

                    if (relationship != null) {
                        addRelationshipToView(relationship, view);
                    }
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
                String token = tokens.get(i);

                if (isElementExpression(token)) {
                    new DeploymentViewExpressionParser().parseElementExpression(token, context).forEach(e -> removeElementFromView(e, view));
                } else if (isRelationshipExpression(token)) {
                    new DeploymentViewExpressionParser().parseRelationshipExpression(token, context).forEach(r -> removeRelationshipFromView(r, view));
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

    private void addElementToView(Element element, DeploymentView view, String identifier) {
        try {
            if (element instanceof CustomElement) {
                view.add((CustomElement) element);
            } else if (element instanceof DeploymentNode) {
                view.add((DeploymentNode) element);
            } else if (element instanceof InfrastructureNode) {
                view.add((InfrastructureNode) element);
            } else if (element instanceof SoftwareSystem) {
                // find instances of this software system
                view.getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).map(e -> (SoftwareSystemInstance)e).filter(ssi -> ssi.getSoftwareSystem().equals(element) && ssi.getEnvironment().equals(view.getEnvironment())).forEach(view::add);
            } else if (element instanceof SoftwareSystemInstance) {
                view.add((SoftwareSystemInstance) element);
            } else if (element instanceof Container) {
                // find instances of this container
                view.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getContainer().equals(element) && ci.getEnvironment().equals(view.getEnvironment())).forEach(view::add);
            } else if (element instanceof ContainerInstance) {
                view.add((ContainerInstance) element);
            } else {
                if (!StringUtils.isNullOrEmpty(identifier)) {
                    throw new RuntimeException("The element \"" + identifier + "\" can not be added to this type of view");
                }
            }
        } catch (ElementNotPermittedInViewException e) {
            // ignore
        }
    }

    private void removeElementFromView(Element element, DeploymentView view) {
        if (element instanceof CustomElement) {
            view.remove((CustomElement)element);
        } else if (element instanceof DeploymentNode) {
            view.remove((DeploymentNode)element);
        } else if (element instanceof InfrastructureNode) {
            view.remove((InfrastructureNode)element);
        } else if (element instanceof SoftwareSystem) {
            // find instances of this software system
            view.getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).map(e -> (SoftwareSystemInstance)e).filter(ssi -> ssi.getSoftwareSystem().equals(element) && ssi.getEnvironment().equals(view.getEnvironment())).forEach(view::remove);
        } else if (element instanceof SoftwareSystemInstance) {
            view.remove((SoftwareSystemInstance)element);
        } else if (element instanceof Container) {
            // find instances of this container
            view.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getContainer().equals(element) && ci.getEnvironment().equals(view.getEnvironment())).forEach(view::remove);
        } else if (element instanceof ContainerInstance) {
            view.remove((ContainerInstance)element);
        }
    }

    private void addRelationshipToView(Relationship relationship, DeploymentView view) {
        if (view.isElementInView(relationship.getSource()) && view.isElementInView(relationship.getDestination())) {
            view.add(relationship);
        } else {
            // we have a relationship, but the source and/or destination elements are not present in the view
            // if both sides are static structure elements, then perhaps there's a replicated version of the relationship that should be added instead
            Element sourceElement = relationship.getSource();
            Element destinationElement = relationship.getDestination();

            if ((sourceElement instanceof SoftwareSystem || sourceElement instanceof Container) && (destinationElement instanceof SoftwareSystem || destinationElement instanceof Container)) {
                String relationshipId = relationship.getId();

                Set<Relationship> replicatedRelationships = view.getModel().getRelationships().stream().filter(r -> relationshipId.equals(r.getLinkedRelationshipId())).collect(Collectors.toSet());
                for (Relationship replicatedRelationship : replicatedRelationships) {
                    if (view.isElementInView(replicatedRelationship.getSource()) && view.isElementInView(replicatedRelationship.getDestination())) {
                        view.add(replicatedRelationship);
                    }
                }
            }
        }
    }

    private void removeRelationshipFromView(Relationship relationship, DeploymentView view) {
        // remove the specified relationship
        view.remove(relationship);

        // and also remove any replicated versions of the specified relationship
        Collection<Relationship> replicatedRelationshipsInView = view.getRelationships().stream().map(RelationshipView::getRelationship).filter(r -> r.getLinkedRelationshipId() != null && r.getLinkedRelationshipId().equals(relationship.getId())).collect(Collectors.toSet());
        for (Relationship r : replicatedRelationshipsInView) {
            view.remove(r);
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