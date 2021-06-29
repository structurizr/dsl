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

    protected IdentifersRegister identifersRegister = new IdentifersRegister();

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

    void setIdentifierRegister(IdentifersRegister identifersRegister) {
        this.identifersRegister = identifersRegister;
    }

    Element getElement(String identifier) {
        Element element = identifersRegister.getElement(identifier.toLowerCase());

        if (element == null && identifersRegister.getIdentifierScope() == IdentifierScope.Hierarchical) {
            if (this instanceof ModelItemDslContext) {
                ModelItemDslContext modelItemDslContext = (ModelItemDslContext)this;
                if (modelItemDslContext.getModelItem() instanceof Element) {
                    Element parent = (Element)modelItemDslContext.getModelItem();
                    while (parent != null && element == null) {
                        String parentIdentifier = identifersRegister.findIdentifier(parent);

                        element = identifersRegister.getElement(parentIdentifier + "." + identifier);
                        parent = parent.getParent();
                    }
                }
            }
        }

        return element;
    }

    Relationship getRelationship(String identifier) {
        return identifersRegister.getRelationship(identifier.toLowerCase());
    }

    void end() {
    }

}