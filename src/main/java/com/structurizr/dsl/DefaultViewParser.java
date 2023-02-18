package com.structurizr.dsl;

import com.structurizr.view.View;

final class DefaultViewParser extends AbstractParser {

    void parse(ViewDslContext context) {
        View view = context.getView();
        context.getWorkspace().getViews().getConfiguration().setDefaultView(view);
    }

}