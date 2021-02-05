package com.structurizr.dsl;

import com.structurizr.model.Element;

import java.util.Set;

class DeploymentEnvironment extends Element {

    private String name;

    DeploymentEnvironment(String name) {
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
    protected Set<String> getRequiredTags() {
        return null;
    }

}