package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.view.CustomView;

final class CustomViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "custom [key] [title] [description] {";

    private static final String VIEW_TYPE = "Custom";

    private static final int KEY_INDEX = 1;
    private static final int TITLE_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;

    CustomView parse(DslContext context, Tokens tokens) {
        // custom [key] [title] [description]

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        Workspace workspace = context.getWorkspace();
        String key = "";
        String title = "";
        String description = "";

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = workspace.getViews().generateViewKey(VIEW_TYPE);
        }
        validateViewKey(key);

        if (tokens.includes(TITLE_INDEX)) {
            title = tokens.get(TITLE_INDEX);
        }

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        CustomView view = workspace.getViews().createCustomView(key, title, description);

        return view;
    }

}