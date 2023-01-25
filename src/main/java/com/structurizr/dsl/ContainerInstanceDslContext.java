package com.structurizr.dsl;

import com.structurizr.model.ContainerInstance;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElementInstance;

final class ContainerInstanceDslContext extends StaticStructureElementInstanceDslContext {

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

    @Override
    StaticStructureElementInstance getElementInstance() {
        return getContainerInstance();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.RELATIONSHIP_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN,
                StructurizrDslTokens.HEALTH_CHECK_TOKEN
        };
    }

}