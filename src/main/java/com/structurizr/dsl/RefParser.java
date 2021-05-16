package com.structurizr.dsl;

import com.structurizr.model.Element;

final class RefParser extends AbstractParser {

    private static final String GRAMMAR = "ref <canonical name>";

    private final static int NAME_INDEX = 1;

    Element parse(DslContext context, Tokens tokens) {
        // ref <canonical name>

        if (tokens.hasMoreThan(NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String canonicalName = tokens.get(NAME_INDEX);
        Element element = context.getWorkspace().getModel().getElementWithCanonicalName(canonicalName);

        if (element == null) {
            throw new RuntimeException(canonicalName + " could not be found");
        }

        return element;
    }

}