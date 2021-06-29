package com.structurizr.dsl;

final class IdentifierScopeParser extends AbstractParser {

    private static final String GRAMMAR = "!identifiers <flat|hierarchical>";

    private static final int MODE_INDEX = 1;

    IdentifierScope parse(DslContext context, Tokens tokens) {
        // !identifiers <flat|hierarchical>

        if (tokens.hasMoreThan(MODE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(MODE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(MODE_INDEX);
        if ("flat".equalsIgnoreCase(name)) {
            return IdentifierScope.Flat;
        } else if ("hierarchical".equalsIgnoreCase(name)) {
            return IdentifierScope.Hierarchical;
        } else {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }
    }

}