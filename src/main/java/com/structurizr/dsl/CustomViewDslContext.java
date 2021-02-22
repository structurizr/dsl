package com.structurizr.dsl;

import com.structurizr.view.CustomView;

final class CustomViewDslContext extends ViewDslContext {

    CustomViewDslContext(CustomView view) {
        super(view);
    }

    public CustomView getCustomView() {
        return (CustomView)super.getView();
    }

}
