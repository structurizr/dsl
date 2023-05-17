package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystemInstance;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElementInstance;

import javax.annotation.Nonnull;

final class SoftwareSystemInstanceDslContext extends StaticStructureElementInstanceDslContext {

    @Nonnull
    private final SoftwareSystemInstance softwareSystemInstance;

    SoftwareSystemInstanceDslContext(@Nonnull SoftwareSystemInstance softwareSystemInstance) {
        this.softwareSystemInstance = softwareSystemInstance;
    }

    @Nonnull
    SoftwareSystemInstance getSoftwareSystemInstance() {
        return softwareSystemInstance;
    }

    @Nonnull
    @Override
    ModelItem getModelItem() {
        return getSoftwareSystemInstance();
    }

    @Nonnull
    @Override
    StaticStructureElementInstance getElementInstance() {
        return getSoftwareSystemInstance();
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
