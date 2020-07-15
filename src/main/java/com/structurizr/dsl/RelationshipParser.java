package com.structurizr.dsl;

import com.structurizr.model.*;

final class RelationshipParser extends AbstractParser {

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private final static int DESCRIPTION_INDEX = 3;
    private final static int TECHNOLOGY_INDEX = 4;
    private final static int TAGS_INDEX = 5;

    Relationship parse(DslContext context, Tokens tokens) {
        // <identifier> -> <identifier> [description] [technology] [tags]

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: <identifier> -> <identifier> [description] [technology] [tags]");
        }

        Relationship relationship;
        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        if (context.getElement(sourceId) == null) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        }

        if (context.getElement(destinationId) == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        String[] tags = new String[0];
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX).split(",");
        }

        Element sourceElement = context.getElement(sourceId);
        Element destinationElement = context.getElement(destinationId);

        if (sourceElement instanceof StaticStructureElement && destinationElement instanceof StaticStructureElement) {
            relationship = ((StaticStructureElement)sourceElement).uses((StaticStructureElement)destinationElement, description, technology, null, tags);
        } else if (sourceElement instanceof DeploymentNode && destinationElement instanceof DeploymentNode) {
            relationship = ((DeploymentNode)sourceElement).uses((DeploymentNode)destinationElement, description, technology, null, tags);
        } else if (sourceElement instanceof InfrastructureNode && destinationElement instanceof DeploymentElement) {
            relationship = ((InfrastructureNode)sourceElement).uses((DeploymentElement)destinationElement, description, technology, null, tags);
        } else {
            throw new RuntimeException("A relationship between \"" + sourceId + "\" and \"" + destinationId + "\" is not permitted");
        }

        return relationship;
    }

}