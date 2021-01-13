package com.structurizr.dsl;

class GroupParser {

    private final static int NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // group <name>
        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: group <name> {");
        }

        return tokens.get(NAME_INDEX);
    }

}