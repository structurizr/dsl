package com.structurizr.dsl;

import com.structurizr.model.ContainerInstance;
import com.structurizr.model.ModelItem;

final class ContainerInstanceDslContext extends ModelItemDslContext {

    private ContainerInstance containerInstance;

    ContainerInstanceDslContext(ContainerInstance containerInstance) {
        this.containerInstance = containerInstance;
    }

    ContainerInstance getContainerInstance() {
        return containerInstance;
    }

    @Override
    ModelItem getModelItem() {
        return getContainerInstance();
    }

}