package com.structurizr.dsl;

import com.structurizr.model.*;

final class SoftwareSystemInstanceParser extends AbstractParser {

    private static final int IDENTIFIER_INDEX = 1;
    private static final int TAGS_INDEX = 2;

    SoftwareSystemInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // softwareSystemInstance <identifier> [tags]

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: softwareSystemInstance <identifier> [tags]");
        }

        String softwareSystemIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(softwareSystemIdentifier);
        if (element == null) {
            throw new RuntimeException("The software system \"" + softwareSystemIdentifier + "\" does not exist");
        }

        if (element instanceof SoftwareSystem) {
            DeploymentNode deploymentNode = context.getDeploymentNode();
            SoftwareSystemInstance softwareSystemInstance = deploymentNode.add((SoftwareSystem)element);

            if (tokens.includes(TAGS_INDEX)) {
                String tags = tokens.get(TAGS_INDEX);
                softwareSystemInstance.addTags(tags.split(","));
            }

            return softwareSystemInstance;
        } else {
            throw new RuntimeException("The element \"" + softwareSystemIdentifier + "\" is not a software system");
        }
    }

}