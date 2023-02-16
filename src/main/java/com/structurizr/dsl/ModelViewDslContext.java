package com.structurizr.dsl;

import com.structurizr.view.ModelView;

abstract class ModelViewDslContext extends ViewDslContext {

    ModelViewDslContext(ModelView view) {
        super(view);
    }

    ModelView getView() {
        return (ModelView)super.getView();
    }

}