package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

class ElementGroup extends Element {

    private static final String STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME = "structurizr.groupSeparator";

    private Element parent;
    private final ElementGroup parentGroup;
    private final String name;

    private final Set<Element> elements = new HashSet<>();

    ElementGroup(Model model, String name) {
        setModel(model);
        this.name = name;
        this.parentGroup = null;
    }

    ElementGroup(Model model, String name, ElementGroup parentGroup) {
        setModel(model);
        String groupSeparator = getModel().getProperties().getOrDefault(STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME, "");

        if (StringUtils.isNullOrEmpty(groupSeparator)) {
            throw new RuntimeException("To use nested groups, please define a model property named " + STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME);
        }

        this.name = parentGroup.getName() + groupSeparator + name;
        this.parentGroup = parentGroup;
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

        if (parentGroup != null) {
            parentGroup.addElement(element);
        }
    }

    Set<Element> getElements() {
        return new HashSet<>(elements);
    }

}