package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;

final class CustomElementDslContext extends GroupableElementDslContext {

    private CustomElement customElement;

    CustomElementDslContext(CustomElement customElement) {
        this.customElement = customElement;
    }

    CustomElement getCustomElement() {
        return customElement;
    }

    @Override
    ModelItem getModelItem() {
        return getCustomElement();
    }

    @Override
    GroupableElement getElement() {
        return customElement;
    }

}