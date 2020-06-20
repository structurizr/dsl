package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

final class ThemesParser extends AbstractParser {

    private final static int FIRST_THEME_INDEX = 1;

    void parse(DslContext context, Tokens tokens) {
        // themes <themeUrl> [themeUrl] ... [themeUrl]
        if (!tokens.includes(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Expected: themes <themeUrl> [themeUrl] ... [themeUrl]");
        }

        List<String> themes = new ArrayList<>();
        for (int i = FIRST_THEME_INDEX; i < tokens.size(); i++) {
            themes.add(tokens.get(i));
        }

        context.getWorkspace().getViews().getConfiguration().setThemes(themes.toArray(new String[0]));
    }

}