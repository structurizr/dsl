package com.structurizr.dsl;

import com.structurizr.model.Element;

final class ModelItemParser extends AbstractParser {

    private final static int DESCRIPTION_INDEX = 1;

    private final static int TAGS_INDEX = 1;

    private final static int URL_INDEX = 1;

    private final static int PERSPECTIVE_NAME_INDEX = 0;
    private final static int PERSPECTIVE_DESCRIPTION_INDEX = 1;
    private final static int PERSPECTIVE_VALUE_INDEX = 2;

    void parseTags(ModelItemDslContext context, Tokens tokens) {
        // tags <tags> [tags]
        if (!tokens.includes(TAGS_INDEX)) {
            throw new RuntimeException("Expected: tags <tags> [tags]");
        }

        for (int i = TAGS_INDEX; i < tokens.size(); i++) {
            String tags = tokens.get(i);
            context.getModelItem().addTags(tags.split(","));
        }
    }

    void parseDescription(ModelItemDslContext context, Tokens tokens) {
        // description <description>
        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <description>");
        }

        if (!tokens.includes(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Expected: description <description>");
        }

        String description = tokens.get(DESCRIPTION_INDEX);
        ((Element)context.getModelItem()).setDescription(description);
    }

    void parseUrl(ModelItemDslContext context, Tokens tokens) {
        // url <url>
        if (tokens.hasMoreThan(URL_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: url <url>");
        }

        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: url <url>");
        }

        String url = tokens.get(URL_INDEX);
        context.getModelItem().setUrl(url);
    }

    void parsePerspective(ModelItemPerspectivesDslContext context, Tokens tokens) {
        // <name> <description> [value]

        if (tokens.hasMoreThan(PERSPECTIVE_VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: <name> <description> [value]");
        }

        if (!tokens.includes(PERSPECTIVE_DESCRIPTION_INDEX)) {
            throw new RuntimeException("Expected: <name> <description> [value]");
        }

        String name = tokens.get(PERSPECTIVE_NAME_INDEX);
        String description = tokens.get(PERSPECTIVE_DESCRIPTION_INDEX);
        String value = "";

        if (tokens.includes(PERSPECTIVE_VALUE_INDEX)) {
            value = tokens.get(PERSPECTIVE_VALUE_INDEX);
        }

        context.getModelItem().addPerspective(name, description, value);
    }

}