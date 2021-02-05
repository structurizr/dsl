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

}