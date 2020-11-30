package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.DynamicView;

final class DynamicViewContentParser extends AbstractParser {

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;

    void parseRelationship(DynamicViewDslContext context, Tokens tokens) {
        // <identifier> -> <identifier> [description]

        DynamicView view = context.getView();

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: <identifier> -> <identifier> [description]");
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        Element sourceElement = context.getElement(sourceId);
        if (sourceElement == null) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        }

        Element destinationElement = context.getElement(destinationId);
        if (destinationElement == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        if (!sourceElement.hasEfferentRelationshipWith(destinationElement) && !destinationElement.hasEfferentRelationshipWith(sourceElement)) {
            new ExplicitRelationshipParser().parse(context, tokens);
        }

        view.add(sourceElement, description, destinationElement);
    }

}