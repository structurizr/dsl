package com.structurizr.dsl;

import com.structurizr.view.CustomView;

class CustomViewAnimationDslContext extends DslContext {

    private CustomView view;

    CustomViewAnimationDslContext(CustomView view) {
        super();

        this.view = view;
    }

    CustomView getView() {
        return view;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ANIMATION_STEP_IN_VIEW_TOKEN
        };
    }

}