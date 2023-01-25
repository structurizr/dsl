package com.structurizr.dsl;

import com.structurizr.view.StaticView;

class StaticViewAnimationDslContext extends DslContext {

    private StaticView view;

    StaticViewAnimationDslContext(StaticView view) {
        super();

        this.view = view;
    }

    StaticView getView() {
        return view;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ANIMATION_STEP_IN_VIEW_TOKEN
        };
    }

}