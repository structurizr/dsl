package com.structurizr.dsl;

import com.structurizr.PropertyHolder;

final class PropertiesDslContext extends DslContext {

    private PropertyHolder propertyHolder;

    public PropertiesDslContext(PropertyHolder propertyHolder) {
        this.propertyHolder = propertyHolder;
    }

    PropertyHolder getPropertyHolder() {
        return this.propertyHolder;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}