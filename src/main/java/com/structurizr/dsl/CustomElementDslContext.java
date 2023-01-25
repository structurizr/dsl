package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;

final class CustomElementDslContext extends GroupableElementDslContext {

    private CustomElement customElement;

    CustomElementDslContext(CustomElement customElement) {
        this.customElement = customElement;
    }

    CustomElement getCustomElement() {
        return customElement;
    }

    @Override
    ModelItem getModelItem() {
        return getCustomElement();
    }

    @Override
    GroupableElement getElement() {
        return customElement;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}