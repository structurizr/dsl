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

}