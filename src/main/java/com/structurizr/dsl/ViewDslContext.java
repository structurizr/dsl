package com.structurizr.dsl;

import com.structurizr.view.View;

abstract class ViewDslContext extends DslContext {

    private final View view;

    ViewDslContext(View view) {
        this.view = view;
    }

    View getView() {
        return view;
    }

}