package com.structurizr.dsl;

final class ModelItemParser extends AbstractParser {

    private final static int URL_INDEX = 1;

    private final static int PROPERTY_NAME_INDEX = 0;
    private final static int PROPERTY_VALUE_INDEX = 1;

    private final static int PERSPECTIVE_NAME_INDEX = 0;
    private final static int PERSPECTIVE_DESCRIPTION_INDEX = 1;

    void parseUrl(ModelItemDslContext context, Tokens tokens) {
        // url <url>
        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: url <url>");
        }

        String url = tokens.get(URL_INDEX);
        context.getModelItem().setUrl(url);
    }

    void parseProperty(ModelItemPropertiesDslContext context, Tokens tokens) {
        // <name> <value>

        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: <name> <value>");
        }

        String name = tokens.get(PROPERTY_NAME_INDEX);
        String value = tokens.get(PROPERTY_VALUE_INDEX);

        context.getModelItem().addProperty(name, value);
    }

    void parsePerspective(ModelItemPerspectivesDslContext context, Tokens tokens) {
        // <name> <description>

        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: <name> <description>");
        }

        String name = tokens.get(PERSPECTIVE_NAME_INDEX);
        String value = tokens.get(PERSPECTIVE_DESCRIPTION_INDEX);

        context.getModelItem().addPerspective(name, value);
    }

}