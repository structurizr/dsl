package com.structurizr.dsl;

import com.structurizr.view.StaticView;

class StaticViewDslContext extends ViewDslContext {

    private StaticView view;

    StaticViewDslContext(StaticView view) {
        super(view);

        this.view = view;
    }

    StaticView getView() {
        return view;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.INCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.EXCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.AUTOLAYOUT_VIEW_TOKEN,
                StructurizrDslTokens.ANIMATION_IN_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}