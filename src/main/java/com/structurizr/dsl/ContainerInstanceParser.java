package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.ContainerInstance;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;

final class ContainerInstanceParser extends AbstractParser {

    private static final int IDENTIFIER_INDEX = 1;
    private static final int TAGS_INDEX = 2;

    ContainerInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // containerInstance <identifier> [tags]

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: containerInstance <identifier> [tags]");
        }

        String containerIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(containerIdentifier);
        if (element == null) {
            throw new RuntimeException("The container \"" + containerIdentifier + "\" does not exist");
        }

        if (element instanceof Container) {
            DeploymentNode deploymentNode = context.getDeploymentNode();
            ContainerInstance containerInstance = deploymentNode.add((Container)element);

            if (tokens.includes(TAGS_INDEX)) {
                String tags = tokens.get(TAGS_INDEX);
                containerInstance.addTags(tags.split(","));
            }

            return containerInstance;
        } else {
            throw new RuntimeException("The element \"" + containerIdentifier + "\" is not a container");
        }
    }

}