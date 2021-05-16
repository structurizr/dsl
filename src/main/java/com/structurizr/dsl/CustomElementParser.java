package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Location;
import com.structurizr.model.Person;

final class CustomElementParser extends AbstractParser {

    private static final String GRAMMAR = "element <name> [metadata] [description] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int METADATA_INDEX = 2;
    private final static int DESCRIPTION_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    CustomElement parse(GroupableDslContext context, Tokens tokens) {
        // element <name> [metadata] [description] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(NAME_INDEX);

        String metadata = "";
        if (tokens.includes(METADATA_INDEX)) {
            metadata = tokens.get(METADATA_INDEX);
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        CustomElement customElement = context.getWorkspace().getModel().addCustomElement(name, metadata, description);

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            customElement.addTags(tags.split(","));
        }

        if (context.hasGroup()) {
            customElement.setGroup(context.getGroup().getName());
        }

        return customElement;
    }

}