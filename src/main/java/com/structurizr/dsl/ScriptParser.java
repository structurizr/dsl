package com.structurizr.dsl;

final class ScriptParser extends AbstractParser {

    private static final String EXTERNAL_GRAMMAR = "!script <filename>";
    private static final String INLINE_GRAMMAR = "!script <language>";

    private static final int FILENAME_INDEX = 1;
    private static final int LANGUAGE_INDEX = 1;

    private final static int PARAMETER_NAME_INDEX = 0;
    private final static int PARAMETER_VALUE_INDEX = 1;

    boolean isInlineScript(Tokens tokens) {
        return
                DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(tokens.get(tokens.size()-1)) &&
                tokens.includes(LANGUAGE_INDEX) &&
                InlineScriptDslContext.SUPPORTED_LANGUAGES.containsKey(tokens.get(LANGUAGE_INDEX).toLowerCase());
    }

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

    void parseParameter(ExternalScriptDslContext context, Tokens tokens) {
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