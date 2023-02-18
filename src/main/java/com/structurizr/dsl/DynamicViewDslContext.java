package com.structurizr.dsl;

import com.structurizr.view.DynamicView;

class DynamicViewDslContext extends ModelViewDslContext {

    DynamicViewDslContext(DynamicView view) {
        super(view);
    }

    DynamicView getView() {
        return (DynamicView)super.getView();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.AUTOLAYOUT_VIEW_TOKEN,
                StructurizrDslTokens.DEFAULT_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}