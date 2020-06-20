package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.DeploymentView;

final class DeploymentViewContentParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    private static final String WILDCARD = "*";

    void parseInclude(DeploymentViewDslContext context, Tokens tokens) {
        // include <*|identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [identifier...]");
        }

        DeploymentView view = context.getView();
        if (tokens.contains(WILDCARD)) {
             view.addAllDeploymentNodes();
        } else {
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String identifier = tokens.get(i);

                Element element = context.getElement(identifier);
                Relationship relationship = context.getRelationship(identifier);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof DeploymentNode) {
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
        // exclude <*|identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...]");
        }

        DeploymentView view = context.getView();
        if (tokens.contains(WILDCARD)) {
            context.getWorkspace().getModel().getDeploymentNodes().stream().filter(dn -> dn.getParent() == null && dn.getEnvironment().equals(view.getEnvironment())).forEach(view::add);
        } else {
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String identifier = tokens.get(i);

                Element element = context.getElement(identifier);
                Relationship relationship = context.getRelationship(identifier);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof DeploymentNode) {
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
                    view.remove(relationship);
                }
            }
        }
    }

}