package com.structurizr.dsl;

class GroupParser {

    private static final String GRAMMAR = "group <name> {";

    private final static int NAME_INDEX = 1;

    ElementGroup parse(GroupableDslContext dslContext, Tokens tokens) {
        // group <name>

        if (tokens.hasMoreThan(NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        ElementGroup group;
        if (dslContext.hasGroup()) {
            group = new ElementGroup(dslContext.getWorkspace().getModel(), tokens.get(NAME_INDEX), dslContext.getGroup());
        } else {
            group = new ElementGroup(dslContext.getWorkspace().getModel(), tokens.get(NAME_INDEX));
        }

        return group;
    }

}