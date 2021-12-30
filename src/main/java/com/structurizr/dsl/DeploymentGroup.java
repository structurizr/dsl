package com.structurizr.dsl;

import com.structurizr.model.Element;

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

    @Override
    public Set<String> getDefaultTags() {
        return null;
    }

}