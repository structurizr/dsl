package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;

final class DeploymentNodeDslContext extends DslContext {

    private DeploymentNode deploymentNode;

    DeploymentNodeDslContext(DeploymentNode deploymentNode) {
        this.deploymentNode = deploymentNode;
    }

    DeploymentNode getDeploymentNode() {
        return deploymentNode;
    }

}