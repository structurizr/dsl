package com.structurizr.dsl;

import com.structurizr.view.View;

final class ViewParser extends AbstractParser {

    private static final String TITLE_GRAMMAR = "title <title>";
    private static final String DESCRIPTION_GRAMMAR = "description <description>";

    private static final int TITLE_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 1;

    void parseTitle(CustomViewDslContext context, Tokens tokens) {
        parseTitle(context.getView(), tokens);
    }

    void parseTitle(StaticViewDslContext context, Tokens tokens) {
        parseTitle(context.getView(), tokens);
    }

    void parseTitle(DynamicViewDslContext context, Tokens tokens) {
        parseTitle(context.getView(), tokens);
    }

    void parseTitle(DeploymentViewDslContext context, Tokens tokens) {
        parseTitle(context.getView(), tokens);
    }

    private void parseTitle(View view, Tokens tokens) {
        // title <title>

        if (tokens.hasMoreThan(TITLE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + TITLE_GRAMMAR);
        }

        if (view != null) {
            if (tokens.size() == 2) {
                String title = tokens.get(TITLE_INDEX);

                view.setTitle(title);
            } else {
                throw new RuntimeException("Expected: " + TITLE_GRAMMAR);
            }
        }
    }

    void parseDescription(CustomViewDslContext context, Tokens tokens) {
        parseDescription(context.getView(), tokens);
    }

    void parseDescription(StaticViewDslContext context, Tokens tokens) {
        parseDescription(context.getView(), tokens);
    }

    void parseDescription(DynamicViewDslContext context, Tokens tokens) {
        parseDescription(context.getView(), tokens);
    }

    void parseDescription(DeploymentViewDslContext context, Tokens tokens) {
        parseDescription(context.getView(), tokens);
    }

    private void parseDescription(View view, Tokens tokens) {
        // description <description>

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + DESCRIPTION_GRAMMAR);
        }

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