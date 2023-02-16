package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

final class DeploymentViewContentParser extends ModelViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    void parseInclude(DeploymentViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [*|identifier...]");
        }

        DeploymentView view = context.getView();

        // include <identifier> [identifier...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.equals(WILDCARD) || token.equals(ELEMENT_WILDCARD)) {
                // include * or include element==*
                view.addDefaultElements();
            } else if (isExpression(token)) {
                new DeploymentViewExpressionParser().parseExpression(token, context).forEach(mi -> addModelItemToView(mi, view, null));
            } else {
                new DeploymentViewExpressionParser().parseIdentifier(token, context).forEach(mi -> addModelItemToView(mi, view, token));
            }
        }
    }

    void parseExclude(DeploymentViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...]");
        }

        DeploymentView view = context.getView();

        // exclude <identifier> [identifier...]
        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (isExpression(token)) {
                new DeploymentViewExpressionParser().parseExpression(token, context).forEach(e -> removeModelItemFromView(e, view));
            } else {
                new DeploymentViewExpressionParser().parseIdentifier(token, context).forEach(mi -> removeModelItemFromView(mi, view));
            }
        }
    }

    private void addModelItemToView(ModelItem modelItem, DeploymentView view, String identifier) {
        if (modelItem instanceof Element) {
            addElementToView((Element)modelItem, view, identifier);
        } else {
            addRelationshipToView((Relationship)modelItem, view);
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

    private void removeModelItemFromView(ModelItem modelItem, DeploymentView view) {
        if (modelItem instanceof Element) {
            removeElementFromView((Element)modelItem, view);
        } else {
            removeRelationshipFromView((Relationship)modelItem, view);
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

    @Override
    protected void removeRelationshipFromView(Relationship relationship, ModelView view) {
        // remove the specified relationship
        view.remove(relationship);

        // and also remove any replicated versions of the specified relationship
        Collection<Relationship> replicatedRelationshipsInView = view.getRelationships().stream().map(RelationshipView::getRelationship).filter(r -> r.getLinkedRelationshipId() != null && r.getLinkedRelationshipId().equals(relationship.getId())).collect(Collectors.toSet());
        for (Relationship r : replicatedRelationshipsInView) {
            view.remove(r);
        }
    }

}