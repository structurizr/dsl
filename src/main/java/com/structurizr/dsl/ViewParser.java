package com.structurizr.dsl;

import com.structurizr.view.View;

final class ViewParser extends AbstractParser {

    private static final String TITLE_GRAMMAR = "title <title>";
    private static final String DESCRIPTION_GRAMMAR = "description <description>";

    private static final int TITLE_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 1;

    void parseTitle(ViewDslContext context, Tokens tokens) {
        // title <title>

        if (tokens.hasMoreThan(TITLE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + TITLE_GRAMMAR);
        }

        View view = context.getView();
        if (view != null) {
            if (tokens.size() == 2) {
                String title = tokens.get(TITLE_INDEX);

                view.setTitle(title);
            } else {
                throw new RuntimeException("Expected: " + TITLE_GRAMMAR);
            }
        }
    }

    void parseDescription(ViewDslContext context, Tokens tokens) {
        // description <description>

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + DESCRIPTION_GRAMMAR);
        }

        View view = context.getView();
        if (view != null) {
            if (tokens.size() == 2) {
                String description = tokens.get(DESCRIPTION_INDEX);

                view.setDescription(description);
            } else {
                throw new RuntimeException("Expected: " + DESCRIPTION_GRAMMAR);
            }
        }
    }

}