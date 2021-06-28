package com.structurizr.dsl;

final class IdentifierScopeParser extends AbstractParser {

    private static final String GRAMMAR = "!identifiers <global|local>";

    private static final int MODE_INDEX = 1;

    IdentifierScope parse(DslContext context, Tokens tokens) {
        // !identifiers <global|local>

        if (tokens.hasMoreThan(MODE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(MODE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(MODE_INDEX);
        if ("global".equalsIgnoreCase(name)) {
            return IdentifierScope.Global;
        } else if ("local".equalsIgnoreCase(name)) {
            return IdentifierScope.Local;
        } else {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }
    }

}