package com.structurizr.dsl;

import com.structurizr.view.View;

final class ViewParser extends AbstractParser {

    private static final String GRAMMAR = "title <title>";

    private static final int TITLE_INDEX = 1;

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
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (view != null) {
            if (tokens.size() == 2) {
                String title = tokens.get(TITLE_INDEX);

                view.setTitle(title);
            } else {
                throw new RuntimeException("Expected: " + GRAMMAR);
            }
        }
    }

}