package com.structurizr.dsl;

class GroupParser {

    private static final String GRAMMAR = "group <name> {";

    private final static int NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // group <name>

        if (tokens.hasMoreThan(NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        return tokens.get(NAME_INDEX);
    }

}