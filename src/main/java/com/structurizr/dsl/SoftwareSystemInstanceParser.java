package com.structurizr.dsl;

import com.structurizr.model.*;

final class SoftwareSystemInstanceParser extends AbstractParser {

    private static final int IDENTIFIER_INDEX = 1;
    private static final int SECOND_TOKEN = 2;
    private static final int THIRD_TOKEN = 3;

    SoftwareSystemInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // softwareSystemInstance <identifier> [tags]
        // softwareSystemInstance <identifier> [deploymentGroup] [tags]

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: softwareSystemInstance <identifier> [deploymentGroup|tags] [tags]");
        }

        String softwareSystemIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(softwareSystemIdentifier);
        if (element == null) {
            throw new RuntimeException("The software system \"" + softwareSystemIdentifier + "\" does not exist");
        }

        if (element instanceof SoftwareSystem) {
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

            SoftwareSystemInstance softwareSystemInstance = deploymentNode.add((SoftwareSystem)element, deploymentGroup);

            if (tokens.includes(tagsIndex)) {
                String tags = tokens.get(tagsIndex);
                softwareSystemInstance.addTags(tags.split(","));
            }

            return softwareSystemInstance;
        } else {
            throw new RuntimeException("The element \"" + softwareSystemIdentifier + "\" is not a software system");
        }
    }

}