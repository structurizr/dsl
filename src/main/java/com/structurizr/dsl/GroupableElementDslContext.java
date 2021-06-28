package com.structurizr.dsl;

import com.structurizr.model.GroupableElement;

abstract class GroupableElementDslContext extends ModelItemDslContext {

    GroupableElementDslContext() {
        super();
    }

    GroupableElementDslContext(ElementGroup group) {
        super(group);
    }

    abstract GroupableElement getElement();

}