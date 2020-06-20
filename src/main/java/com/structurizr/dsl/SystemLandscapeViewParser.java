package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.view.SystemLandscapeView;

final class SystemLandscapeViewParser extends AbstractParser {

    private static final String VIEW_TYPE = "SystemLandscape";

    private static final int KEY_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;

    SystemLandscapeView parse(DslContext context, Tokens tokens) {
        // systemLandscape [key] [description]

        Workspace workspace = context.getWorkspace();
        String key = "";
        String description = "";

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = generateViewKey(workspace, VIEW_TYPE);
        }
        validateViewKey(key);

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView(key, description);
        view.setEnterpriseBoundaryVisible(true);

        return view;
    }

}