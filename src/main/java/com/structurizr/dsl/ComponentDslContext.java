package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;

final class ComponentDslContext extends GroupableElementDslContext {

    private Component component;

    ComponentDslContext(Component component) {
        this.component = component;
    }

    Component getComponent() {
        return component;
    }

    @Override
    ModelItem getModelItem() {
        return getComponent();
    }

    @Override
    GroupableElement getElement() {
        return component;
    }

}