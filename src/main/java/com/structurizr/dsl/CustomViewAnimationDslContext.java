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

}