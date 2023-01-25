package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

final class ModelItemPerspectivesDslContext extends DslContext {

    private ModelItem modelItem;

    public ModelItemPerspectivesDslContext(ModelItem modelItem) {
        this.modelItem = modelItem;
    }

    ModelItem getModelItem() {
        return this.modelItem;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}