package com.structurizr.dsl;

import com.structurizr.view.StaticView;

class StaticViewDslContext extends DslContext {

    private StaticView view;

    StaticViewDslContext(StaticView view) {
        this.view = view;
    }

    StaticView getView() {
        return view;
    }

}