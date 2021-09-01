package com.structurizr.dsl;

final class ScriptParser extends AbstractParser {

    private static final String EXTERNAL_GRAMMAR = "!script <filename>";
    private static final String INLINE_GRAMMAR = "!script <language>";

    private static final int FILENAME_INDEX = 1;
    private static final int LANGUAGE_INDEX = 1;

    String parseExternal(Tokens tokens) {
        // !script <filename>

        if (tokens.hasMoreThan(FILENAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + EXTERNAL_GRAMMAR);
        }

        if (!tokens.includes(FILENAME_INDEX)) {
            throw new RuntimeException("Expected: " + EXTERNAL_GRAMMAR);
        }

        return tokens.get(FILENAME_INDEX);
    }

    String parseInline(Tokens tokens) {
        // !script <language>

        if (tokens.hasMoreThan(LANGUAGE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + INLINE_GRAMMAR);
        }

        if (!tokens.includes(LANGUAGE_INDEX)) {
            throw new RuntimeException("Expected: " + INLINE_GRAMMAR);
        }

        return tokens.get(LANGUAGE_INDEX);
    }

}