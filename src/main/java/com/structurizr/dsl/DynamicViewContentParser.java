package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.model.StaticStructureElement;
import com.structurizr.view.DynamicView;
import com.structurizr.view.RelationshipView;

final class DynamicViewContentParser extends AbstractParser {

    private static final String GRAMMAR_1 = "<identifier> -> <identifier> [description] [technology]";
    private static final String GRAMMAR_2 = "<identifier> [description]";

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int RELATIONSHIP_TOKEN_INDEX = 1;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int TECHNOLOGY_INDEX = 4;

    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 0;

    RelationshipView parseRelationship(DynamicViewDslContext context, Tokens tokens) {
        DynamicView view = context.getView();

        if (tokens.size() > 1 && StructurizrDslTokens.RELATIONSHIP_TOKEN.equals(tokens.get(RELATIONSHIP_TOKEN_INDEX))) {
            // <element identifier> -> <element identifier> [description] [technology]
            if (tokens.hasMoreThan(TECHNOLOGY_INDEX)) {
                throw new RuntimeException("Too many tokens, expected: " + GRAMMAR_1);
            }

            if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
                throw new RuntimeException("Expected: " + GRAMMAR_1);
            }

            String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
            String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

            Element sourceElement = context.getElement(sourceId);
            if (sourceElement == null) {
                throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
            }

            if (!(sourceElement instanceof StaticStructureElement || sourceElement instanceof CustomElement)) {
                throw new RuntimeException("The source element \"" + sourceId + "\" should be a static structure or custom element");
            }

            Element destinationElement = context.getElement(destinationId);
            if (destinationElement == null) {
                throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
            }

            if (!(destinationElement instanceof StaticStructureElement || destinationElement instanceof CustomElement)) {
                throw new RuntimeException("The destination element \"" + destinationId + "\" should be a static structure or custom element");
            }

            String description = "";
            if (tokens.includes(DESCRIPTION_INDEX)) {
                description = tokens.get(DESCRIPTION_INDEX);
            }

            String technology = "";
            if (tokens.includes(TECHNOLOGY_INDEX)) {
                technology = tokens.get(TECHNOLOGY_INDEX);
            }

            if (sourceElement instanceof StaticStructureElement && destinationElement instanceof StaticStructureElement) {
                return view.add((StaticStructureElement) sourceElement, description, technology, (StaticStructureElement) destinationElement);
            } else if (sourceElement instanceof StaticStructureElement && destinationElement instanceof CustomElement) {
                return view.add((StaticStructureElement) sourceElement, description, technology, (CustomElement) destinationElement);
            } else if (sourceElement instanceof CustomElement && destinationElement instanceof StaticStructureElement) {
                return view.add((CustomElement) sourceElement, description, technology, (StaticStructureElement) destinationElement);
            } else if (sourceElement instanceof CustomElement && destinationElement instanceof CustomElement) {
                return view.add((CustomElement) sourceElement, description, technology, (CustomElement) destinationElement);
            }
        } else {
            // <relationship identifier> [description] [technology]
            String relationshipId = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX);
            Relationship relationship = context.getRelationship(relationshipId);

            if (tokens.hasMoreThan(RELATIONSHIP_IDENTIFIER_INDEX+1)) {
                throw new RuntimeException("Too many tokens, expected: " + GRAMMAR_2);
            }

            if (relationship == null) {
                throw new RuntimeException("The relationship \"" + relationshipId + "\" does not exist");
            }

            String description = "";
            if (tokens.includes(RELATIONSHIP_IDENTIFIER_INDEX+1)) {
                description = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX+1);
            }

            return view.add(relationship, description);
        }

        throw new RuntimeException("The specified relationship could not be added");
    }

}