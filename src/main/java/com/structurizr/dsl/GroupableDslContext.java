package com.structurizr.dsl;

abstract class GroupableDslContext extends DslContext {

    private ElementGroup group;

    GroupableDslContext() {
        this(null);
    }

    GroupableDslContext(ElementGroup group) {
        this.group = group;
    }

    boolean hasGroup() {
        return group != null;
    }

    ElementGroup getGroup() {
        return group;
    }

}