package com.structurizr.dsl;

import com.structurizr.model.*;

final class ContainerInstanceParser extends AbstractParser {

    private static final int IDENTIFIER_INDEX = 1;
    private static final int SECOND_TOKEN = 2;
    private static final int THIRD_TOKEN = 3;

    ContainerInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // containerInstance <identifier> [tags] [group]

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: containerInstance <identifier> [deploymentGroup|tags] [tags]");
        }

        String containerIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(containerIdentifier);
        if (element == null) {
            throw new RuntimeException("The container \"" + containerIdentifier + "\" does not exist");
        }

        if (element instanceof Container) {
            DeploymentNode deploymentNode = context.getDeploymentNode();

            String deploymentGroup = DeploymentElement.DEFAULT_DEPLOYMENT_GROUP;
            int tagsIndex = SECOND_TOKEN;

            if (tokens.includes(SECOND_TOKEN)) {
                String token = tokens.get(SECOND_TOKEN);

                if (context.getElement(token) instanceof DeploymentGroup) {
                    deploymentGroup = context.getElement(token).getName();
                    tagsIndex = THIRD_TOKEN;
                }
            }

            ContainerInstance containerInstance = deploymentNode.add((Container)element, deploymentGroup);

            if (tokens.includes(tagsIndex)) {
                String tags = tokens.get(tagsIndex);
                containerInstance.addTags(tags.split(","));
            }

            return containerInstance;
        } else {
            throw new RuntimeException("The element \"" + containerIdentifier + "\" is not a container");
        }
    }

}