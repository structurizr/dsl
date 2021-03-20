package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.ElementNotPermittedInViewException;
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
                String token = tokens.get(i);

                // assume the token is an identifier
                Element element = context.getElement(token);
                Relationship relationship = context.getRelationship(token);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof CustomElement) {
                        view.add((CustomElement) element);
                    } else if (element instanceof DeploymentNode) {
                        view.add((DeploymentNode) element);
                    } else if (element instanceof InfrastructureNode) {
                        view.add((InfrastructureNode) element);
                    } else if (element instanceof SoftwareSystemInstance) {
                        view.add((SoftwareSystemInstance) element);
                    } else if (element instanceof ContainerInstance) {
                        view.add((ContainerInstance) element);
                    } else {
                        throw new RuntimeException("The element \"" + token + "\" can not be added to this view");
                    }
                }

                if (relationship != null) {
                    view.add(relationship);
                }
            }
        }
    }

    private void addElementToView(Element element, DeploymentView view) {
        try {
            if (element instanceof DeploymentNode) {
                view.add((DeploymentNode) element);
            } else if (element instanceof InfrastructureNode) {
                view.add((InfrastructureNode) element);
            } else if (element instanceof SoftwareSystemInstance) {
                view.add((SoftwareSystemInstance) element);
            } else if (element instanceof ContainerInstance) {
                view.add((ContainerInstance) element);
            }
        } catch (ElementNotPermittedInViewException e) {
            // ignore
        }
    }

    private void removeElementFromView(Element element, DeploymentView view) {
        if (element instanceof DeploymentNode) {
            view.remove((DeploymentNode)element);
        } else if (element instanceof InfrastructureNode) {
            view.remove((InfrastructureNode)element);
        } else if (element instanceof SoftwareSystemInstance) {
            view.remove((SoftwareSystemInstance)element);
        } else if (element instanceof ContainerInstance) {
            view.remove((ContainerInstance)element);
        }
    }

    private boolean hasAllTags(ModelItem modelItem, String[] tags) {
        boolean result = true;

        for (String tag : tags) {
            result = result && modelItem.hasTag(tag.trim());
        }

        return result;
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

                // assume the token is an identifier
                Element element = context.getElement(token);
                Relationship relationship = context.getRelationship(token);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + token + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof DeploymentNode) {
                        view.remove((DeploymentNode) element);
                    } else if (element instanceof InfrastructureNode) {
                        view.remove((InfrastructureNode) element);
                    } else if (element instanceof SoftwareSystemInstance) {
                        view.remove((SoftwareSystemInstance) element);
                    } else if (element instanceof ContainerInstance) {
                        view.remove((ContainerInstance) element);
                    } else {
                        throw new RuntimeException("The element \"" + token + "\" can not be added to this view");
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