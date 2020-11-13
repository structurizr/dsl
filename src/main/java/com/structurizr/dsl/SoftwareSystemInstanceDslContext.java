package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystemInstance;
import com.structurizr.model.ModelItem;

final class SoftwareSystemInstanceDslContext extends ModelItemDslContext {

    private SoftwareSystemInstance softwareSystemInstance;

    SoftwareSystemInstanceDslContext(SoftwareSystemInstance softwareSystemInstance) {
        this.softwareSystemInstance = softwareSystemInstance;
    }

    SoftwareSystemInstance getSoftwareSystemInstance() {
        return softwareSystemInstance;
    }

    @Override
    ModelItem getModelItem() {
        return getSoftwareSystemInstance();
    }

}