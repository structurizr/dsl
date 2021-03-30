package com.structurizr.dsl;

final class ConstantParser extends AbstractParser {

    private static final String GRAMMAR = "!constant <name> <value>";

    private static final int NAME_INDEX = 1;
    private static final int VALUE_INDEX = 2;

    private static final String NAME_REGEX = "[a-zA-Z0-9-_.]+";

    Constant parse(DslContext context, Tokens tokens) {
        // !constant name value

        if (tokens.hasMoreThan(VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(VALUE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(NAME_INDEX);
        String value = tokens.get(VALUE_INDEX);

        if (!name.matches(NAME_REGEX)) {
            throw new RuntimeException("Constant names must only contain the following characters: a-zA-Z0-9-_.");
        }

        return new Constant(name, value);
    }

}