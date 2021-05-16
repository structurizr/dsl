package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.HashMap;
import java.util.Map;

abstract class DslContext {

    static final String CONTEXT_START_TOKEN = "{";
    static final String CONTEXT_END_TOKEN = "}";

    private Workspace workspace;
    private boolean extendingWorkspace;

    protected Map<String, Element> elements = new HashMap<>();
    protected Map<String, Relationship> relationships = new HashMap<>();

    Workspace getWorkspace() {
        return workspace;
    }

    void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    boolean isExtendingWorkspace() {
        return extendingWorkspace;
    }

    void setExtendingWorkspace(boolean extendingWorkspace) {
        this.extendingWorkspace = extendingWorkspace;
    }

    Element getElement(String identifier) {
        return elements.get(identifier.toLowerCase());
    }

    void setElements(Map<String, Element> elements) {
        this.elements = elements;
    }

    Relationship getRelationship(String identifier) {
        return relationships.get(identifier.toLowerCase());
    }

    void setRelationships(Map<String, Relationship> relationships) {
        this.relationships = relationships;
    }

    void end() {
    }

}