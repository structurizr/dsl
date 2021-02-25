package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystemInstance;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElementInstance;

final class SoftwareSystemInstanceDslContext extends StaticStructureElementInstanceDslContext {

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

    @Override
    StaticStructureElementInstance getElementInstance() {
        return getSoftwareSystemInstance();
    }

}