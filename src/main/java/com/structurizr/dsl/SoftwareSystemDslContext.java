package com.structurizr.dsl;

import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;
import com.structurizr.model.SoftwareSystem;

final class SoftwareSystemDslContext extends GroupableElementDslContext {

    private SoftwareSystem softwareSystem;

    SoftwareSystemDslContext(SoftwareSystem softwareSystem) {
        this(softwareSystem, null);
    }

    SoftwareSystemDslContext(SoftwareSystem softwareSystem, ElementGroup group) {
        super(group);

        this.softwareSystem = softwareSystem;
    }

    SoftwareSystem getSoftwareSystem() {
        return softwareSystem;
    }

    @Override
    ModelItem getModelItem() {
        return getSoftwareSystem();
    }

    @Override
    GroupableElement getElement() {
        return softwareSystem;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.DOCS_TOKEN,
                StructurizrDslTokens.ADRS_TOKEN,
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.CONTAINER_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}