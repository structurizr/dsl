package com.structurizr.dsl;

import com.structurizr.view.DynamicView;

final class DynamicViewDslContext extends DslContext {

    private DynamicView view;

    DynamicViewDslContext(DynamicView view) {
        this.view = view;
    }

    DynamicView getView() {
        return view;
    }

}