package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;

final class ContainerDslContext extends GroupableElementDslContext {

    private Container container;

    ContainerDslContext(Container container) {
        this(container, null);
    }

    ContainerDslContext(Container container, ElementGroup group) {
        super(group);

        this.container = container;
    }

    Container getContainer() {
        return container;
    }

    @Override
    ModelItem getModelItem() {
        return getContainer();
    }

    @Override
    GroupableElement getElement() {
        return container;
    }

}