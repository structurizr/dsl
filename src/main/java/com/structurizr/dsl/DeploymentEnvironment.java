package com.structurizr.dsl;

import com.structurizr.model.Element;

import java.util.Objects;
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
    public Set<String> getDefaultTags() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeploymentEnvironment that = (DeploymentEnvironment) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}