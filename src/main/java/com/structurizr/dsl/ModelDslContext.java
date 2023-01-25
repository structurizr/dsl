package com.structurizr.dsl;

import com.structurizr.model.Location;

final class ModelDslContext extends GroupableDslContext {

    ModelDslContext() {
        super(null);
    }

    ModelDslContext(ElementGroup group) {
        super(group);
    }

    @Override
    void end() {
        // the location is only set to internal when created inside an "enterprise" block, let's assume the rest are external if an enterprise has been specified
        if (getWorkspace().getModel().getEnterprise() != null) {
            getWorkspace().getModel().getPeople().stream().filter(p -> p.getLocation() != Location.Internal).forEach(p -> p.setLocation(Location.External));
            getWorkspace().getModel().getSoftwareSystems().stream().filter(ss -> ss.getLocation() != Location.Internal).forEach(ss -> ss.setLocation(Location.External));
        }
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ENTERPRISE_TOKEN,
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.PERSON_TOKEN,
                StructurizrDslTokens.SOFTWARE_SYSTEM_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_ENVIRONMENT_TOKEN,
                StructurizrDslTokens.CUSTOM_ELEMENT_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}