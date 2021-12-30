package com.structurizr.dsl;

import com.structurizr.model.Element;

import java.util.HashSet;
import java.util.Set;

class ElementGroup extends Element {

    private Element parent;
    private String name;

    private Set<Element> elements = new HashSet<>();

    ElementGroup(String name) {
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

    void setParent(Element parent) {
        this.parent = parent;
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public Set<String> getDefaultTags() {
        return null;
    }

    void addElement(Element element) {
        elements.add(element);
    }

    Set<Element> getElements() {
        return new HashSet<>(elements);
    }

}