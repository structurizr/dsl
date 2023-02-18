package com.structurizr.dsl;

import com.structurizr.view.CustomView;

final class CustomViewDslContext extends ModelViewDslContext {

    CustomViewDslContext(CustomView view) {
        super(view);
    }

    public CustomView getCustomView() {
        return (CustomView)super.getView();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.INCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.EXCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.AUTOLAYOUT_VIEW_TOKEN,
                StructurizrDslTokens.DEFAULT_VIEW_TOKEN,
                StructurizrDslTokens.ANIMATION_IN_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}
