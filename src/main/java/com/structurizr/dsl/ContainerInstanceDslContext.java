package com.structurizr.dsl;

import com.structurizr.model.ContainerInstance;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElementInstance;

import javax.annotation.Nonnull;

final class ContainerInstanceDslContext extends StaticStructureElementInstanceDslContext {

    @Nonnull
    private ContainerInstance containerInstance;

    ContainerInstanceDslContext(@Nonnull ContainerInstance containerInstance) {
        this.containerInstance = containerInstance;
    }

    @Nonnull
    ContainerInstance getContainerInstance() {
        return containerInstance;
    }

    @Nonnull
    @Override
    ModelItem getModelItem() {
        return getContainerInstance();
    }

    @Nonnull
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
