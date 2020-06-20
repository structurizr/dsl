package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;

final class SoftwareSystemDslContext extends DslContext {

    private SoftwareSystem softwareSystem;

    SoftwareSystemDslContext(SoftwareSystem softwareSystem) {
        this.softwareSystem = softwareSystem;
    }

    SoftwareSystem getSoftwareSystem() {
        return softwareSystem;
    }

}