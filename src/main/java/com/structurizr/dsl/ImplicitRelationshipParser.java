package com.structurizr.dsl;

import com.structurizr.model.*;

final class ImplicitRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "-> <identifier> [description] [technology] [tags]";

    private static final int DESTINATION_IDENTIFIER_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TECHNOLOGY_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    Relationship parse(ModelItemDslContext context, Tokens tokens) {
        // -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        Element sourceElement = (Element)context.getModelItem();
        Element destinationElement = context.getElement(destinationId);

        if (destinationElement == null) {
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

        return createRelationship(sourceElement, description, technology, tags, destinationElement);
    }

}