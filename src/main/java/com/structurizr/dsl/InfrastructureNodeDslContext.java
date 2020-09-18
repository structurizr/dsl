package com.structurizr.dsl;

import com.structurizr.model.InfrastructureNode;
import com.structurizr.model.ModelItem;

final class InfrastructureNodeDslContext extends ModelItemDslContext {

    private InfrastructureNode infrastructureNode;

    InfrastructureNodeDslContext(InfrastructureNode infrastructureNode) {
        this.infrastructureNode = infrastructureNode;
    }

    InfrastructureNode getInfrastructureNode() {
        return infrastructureNode;
    }

    @Override
    ModelItem getModelItem() {
        return getInfrastructureNode();
    }

}