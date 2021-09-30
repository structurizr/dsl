package com.structurizr.dsl;

import com.structurizr.model.*;

final class ExplicitRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "<identifier> -> <identifier> [description] [technology] [tags]";

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private final static int DESCRIPTION_INDEX = 3;
    private final static int TECHNOLOGY_INDEX = 4;
    private final static int TAGS_INDEX = 5;

    Relationship parse(DslContext context, Tokens tokens) {
        // <identifier> -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        Element sourceElement = context.getElement(sourceId);
        Element destinationElement = context.getElement(destinationId);

        if (sourceElement == null) {
            if (StructurizrDslTokens.THIS_TOKEN.equalsIgnoreCase(sourceId) && context instanceof GroupableElementDslContext) {
                GroupableElementDslContext groupableElementDslContext = (GroupableElementDslContext)context;
                sourceElement = groupableElementDslContext.getElement();
            } else {
                throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
            }
        }

        if (destinationElement == null) {
            if (StructurizrDslTokens.THIS_TOKEN.equalsIgnoreCase(destinationId) && context instanceof ModelItemDslContext) {
                ModelItemDslContext modelItemDslContext = (ModelItemDslContext) context;
                if (modelItemDslContext.getModelItem() instanceof Element) {
                    destinationElement = (Element)modelItemDslContext.getModelItem();
                }
            }
        }

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