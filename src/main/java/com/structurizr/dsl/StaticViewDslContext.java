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

}