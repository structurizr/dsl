package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.ModelItem;

final class ContainerDslContext extends ModelItemDslContext {

    private Container container;

    ContainerDslContext(Container container) {
        this.container = container;
    }

    Container getContainer() {
        return container;
    }

    @Override
    ModelItem getModelItem() {
        return getContainer();
    }

}