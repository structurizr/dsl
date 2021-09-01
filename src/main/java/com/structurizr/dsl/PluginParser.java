package com.structurizr.dsl;

final class PluginParser extends AbstractParser {

    private static final String GRAMMAR = "!plugin <fqn>";

    private static final int FQN_INDEX = 1;

    private final static int PARAMETER_NAME_INDEX = 0;
    private final static int PARAMETER_VALUE_INDEX = 1;

    String parse(DslContext context, Tokens tokens) {
        // !plugin <fqn>

        if (tokens.hasMoreThan(FQN_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FQN_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        return tokens.get(FQN_INDEX);
    }

    void parseParameter(PluginDslContext context, Tokens tokens) {
        // <name> <value>

        if (tokens.hasMoreThan(PARAMETER_VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: <name> <value>");
        }

        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: <name> <value>");
        }

        String name = tokens.get(PARAMETER_NAME_INDEX);
        String value = tokens.get(PARAMETER_VALUE_INDEX);

        context.addParameter(name, value);
    }

}