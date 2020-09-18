package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

final class ModelItemPropertiesDslContext extends DslContext {

    private ModelItem modelItem;

    public ModelItemPropertiesDslContext(ModelItem modelItem) {
        this.modelItem = modelItem;
    }

    ModelItem getModelItem() {
        return this.modelItem;
    }

}