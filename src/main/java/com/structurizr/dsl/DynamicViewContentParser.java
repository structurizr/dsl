package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.StaticStructureElement;
import com.structurizr.view.DynamicView;

final class DynamicViewContentParser extends AbstractParser {

    private static final String GRAMMAR = "<identifier> -> <identifier> [description] [technology]";

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int TECHNOLOGY_INDEX = 4;

    void parseRelationship(DynamicViewDslContext context, Tokens tokens) {
        // <identifier> -> <identifier> [description] [technology]

        DynamicView view = context.getView();

        if (tokens.hasMoreThan(TECHNOLOGY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        Element sourceElement = context.getElement(sourceId);
        if (sourceElement == null) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        }

        if (!(sourceElement instanceof StaticStructureElement)) {
            throw new RuntimeException("The source element \"" + sourceId + "\" should be a static structure element");
        }

        Element destinationElement = context.getElement(destinationId);
        if (destinationElement == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        }

        if (!(destinationElement instanceof StaticStructureElement)) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" should be a static structure element");
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        if (!sourceElement.hasEfferentRelationshipWith(destinationElement) && !destinationElement.hasEfferentRelationshipWith(sourceElement)) {
            new ExplicitRelationshipParser().parse(context, tokens);
        }

        view.add((StaticStructureElement)sourceElement, description, technology, (StaticStructureElement)destinationElement);
    }

}