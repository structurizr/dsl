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

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.DEPLOYMENT_NODE_TOKEN,
                StructurizrDslTokens.INFRASTRUCTURE_NODE_TOKEN,
                StructurizrDslTokens.SOFTWARE_SYSTEM_INSTANCE_TOKEN,
                StructurizrDslTokens.CONTAINER_INSTANCE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TECHNOLOGY_TOKEN,
                StructurizrDslTokens.INSTANCES_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN,
        };
    }

}