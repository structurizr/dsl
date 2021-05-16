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
        // the location is only set to internal when created inside an "enterprise" block
        // - if some elements have been marked as internal, let's assume the rest are external
        boolean modelHasInternalPeople = getWorkspace().getModel().getPeople().stream().anyMatch(p -> p.getLocation() == Location.Internal);
        if (modelHasInternalPeople)  {
            getWorkspace().getModel().getPeople().stream().filter(p -> p.getLocation() != Location.Internal).forEach(p -> p.setLocation(Location.External));
        }

        boolean modelHasInternalSoftwareSystems = getWorkspace().getModel().getSoftwareSystems().stream().anyMatch(ss -> ss.getLocation() == Location.Internal);
        if (modelHasInternalSoftwareSystems)  {
            getWorkspace().getModel().getSoftwareSystems().stream().filter(ss -> ss.getLocation() != Location.Internal).forEach(ss -> ss.setLocation(Location.External));
        }
    }

}