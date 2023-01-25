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

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.RELATIONSHIP_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TECHNOLOGY_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN
        };
    }

}