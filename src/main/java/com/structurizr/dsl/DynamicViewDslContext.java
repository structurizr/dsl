package com.structurizr.dsl;

import com.structurizr.view.DynamicView;

class DynamicViewDslContext extends ViewDslContext {

    private DynamicView view;

    DynamicViewDslContext(DynamicView view) {
        super(view);

        this.view = view;
    }

    DynamicView getView() {
        return view;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.AUTOLAYOUT_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}