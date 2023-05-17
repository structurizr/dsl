package com.structurizr.dsl;

import com.structurizr.model.Element;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

class DeploymentGroup extends Element {

    private String name;

    DeploymentGroup(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCanonicalName() {
        return name;
    }

    @Override
    public Element getParent() {
        return null;
    }

    @Nonnull
    @Override
    public Set<String> getDefaultTags() {
        return Collections.emptySet();
    }

}
