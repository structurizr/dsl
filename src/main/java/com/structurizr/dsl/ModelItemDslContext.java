package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

abstract class ModelItemDslContext extends GroupableDslContext {

    ModelItemDslContext() {
        super();
    }

    ModelItemDslContext(ElementGroup group) {
        super(group);
    }

    abstract ModelItem getModelItem();

}