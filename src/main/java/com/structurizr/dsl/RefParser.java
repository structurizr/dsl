package com.structurizr.dsl;

import com.structurizr.model.Element;

final class RefParser extends AbstractParser {

    private static final String GRAMMAR = "!ref <identifier|canonical name>";

    private final static int IDENTIFIER_INDEX = 1;

    Element parse(DslContext context, Tokens tokens) {
        // !ref <identifier|canonical name>

        if (tokens.hasMoreThan(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String s = tokens.get(IDENTIFIER_INDEX);

        Element element;
        if (s.contains("://")) {
            element = context.getWorkspace().getModel().getElementWithCanonicalName(s);
        } else {
            element = context.getElement(s);
        }

        if (element == null) {
            throw new RuntimeException("An element referenced by \"" + s + "\" could not be found");
        }

        return element;
    }

}