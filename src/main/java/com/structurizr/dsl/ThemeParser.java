package com.structurizr.dsl;

final class ThemeParser extends AbstractParser {

    private static final String DEFAULT_THEME_URL = "https://static.structurizr.com/themes/default/theme.json";

    private final static int FIRST_THEME_INDEX = 1;

    void parseTheme(DslContext context, Tokens tokens) {
        // theme <default|url>
        if (tokens.hasMoreThan(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: theme <url>");
        }

        if (!tokens.includes(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Expected: theme <url>");
        }

        addTheme(context, tokens.get(FIRST_THEME_INDEX));
    }

    void parseThemes(DslContext context, Tokens tokens) {
        // themes <url> [url] ... [url]
        if (!tokens.includes(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Expected: themes <url> [url] ... [url]");
        }

        for (int i = FIRST_THEME_INDEX; i < tokens.size(); i++) {
            addTheme(context, tokens.get(i));
        }
    }

    private void addTheme(DslContext context, String url) {
        if ("default".equalsIgnoreCase(url)) {
            url = DEFAULT_THEME_URL;
        }

        context.getWorkspace().getViews().getConfiguration().addTheme(url);
    }

}