package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.ModelItem;

final class DeploymentNodeDslContext extends ModelItemDslContext {

    private DeploymentNode deploymentNode;

    DeploymentNodeDslContext(DeploymentNode deploymentNode) {
        this.deploymentNode = deploymentNode;
    }

    DeploymentNode getDeploymentNode() {
        return deploymentNode;
    }

    @Override
    ModelItem getModelItem() {
        return getDeploymentNode();
    }

}