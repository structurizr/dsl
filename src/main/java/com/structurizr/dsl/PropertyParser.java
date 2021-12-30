package com.structurizr.dsl;

final class PropertyParser extends AbstractParser {

    private final static int PROPERTY_NAME_INDEX = 0;
    private final static int PROPERTY_VALUE_INDEX = 1;

    void parse(PropertiesDslContext context, Tokens tokens) {
        // <name> <value>

        if (tokens.hasMoreThan(PROPERTY_VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: <name> <value>");
        }

        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: <name> <value>");
        }

        String name = tokens.get(PROPERTY_NAME_INDEX);
        String value = tokens.get(PROPERTY_VALUE_INDEX);

        context.getPropertyHolder().addProperty(name, value);
    }

}