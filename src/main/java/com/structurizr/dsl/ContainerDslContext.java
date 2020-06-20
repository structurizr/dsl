package com.structurizr.dsl;

import com.structurizr.model.Container;

final class ContainerDslContext extends DslContext {

    private Container container;

    ContainerDslContext(Container container) {
        this.container = container;
    }

    Container getContainer() {
        return container;
    }

}