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

}