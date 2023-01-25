package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;

final class ContainerDslContext extends GroupableElementDslContext {

    private Container container;

    ContainerDslContext(Container container) {
        this(container, null);
    }

    ContainerDslContext(Container container, ElementGroup group) {
        super(group);

        this.container = container;
    }

    Container getContainer() {
        return container;
    }

    @Override
    ModelItem getModelItem() {
        return getContainer();
    }

    @Override
    GroupableElement getElement() {
        return container;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.DOCS_TOKEN,
                StructurizrDslTokens.ADRS_TOKEN,
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.COMPONENT_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TECHNOLOGY_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}