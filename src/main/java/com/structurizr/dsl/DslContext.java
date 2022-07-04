package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

abstract class DslContext {

    private static final String PLUGINS_DIRECTORY_NAME = "plugins";

    static final String CONTEXT_START_TOKEN = "{";
    static final String CONTEXT_END_TOKEN = "}";

    private Workspace workspace;
    private boolean extendingWorkspace;

    protected IdentifiersRegister identifiersRegister = new IdentifiersRegister();

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

    void setIdentifierRegister(IdentifiersRegister identifersRegister) {
        this.identifiersRegister = identifersRegister;
    }

    Element getElement(String identifier) {
        Element element = null;
        identifier = identifier.toLowerCase();

        if (identifiersRegister.getIdentifierScope() == IdentifierScope.Hierarchical) {
            if (this instanceof ModelItemDslContext) {
                ModelItemDslContext modelItemDslContext = (ModelItemDslContext)this;
                if (modelItemDslContext.getModelItem() instanceof Element) {
                    Element parent = (Element)modelItemDslContext.getModelItem();
                    while (parent != null && element == null) {
                        String parentIdentifier = identifiersRegister.findIdentifier(parent);

                        element = identifiersRegister.getElement(parentIdentifier + "." + identifier);
                        parent = parent.getParent();
                    }
                }
            } else if (this instanceof DeploymentEnvironmentDslContext) {
                DeploymentEnvironmentDslContext deploymentEnvironmentDslContext = (DeploymentEnvironmentDslContext)this;
                DeploymentEnvironment deploymentEnvironment = new DeploymentEnvironment(deploymentEnvironmentDslContext.getEnvironment());
                String parentIdentifier = identifiersRegister.findIdentifier(deploymentEnvironment);

                element = identifiersRegister.getElement(parentIdentifier + "." + identifier);
            }

            if (element == null) {
                // default to finding a top-level element
                element = identifiersRegister.getElement(identifier);
            }
        } else {
            element = identifiersRegister.getElement(identifier);
        }

        return element;
    }

    Relationship getRelationship(String identifier) {
        return identifiersRegister.getRelationship(identifier.toLowerCase());
    }

    protected Class loadClass(String fqn, File dslFile) throws Exception {
        File pluginsDirectory = new File(dslFile.getParent(), PLUGINS_DIRECTORY_NAME);
        URL[] urls = new URL[0];

        if (pluginsDirectory.exists()) {
            File[] jarFiles = pluginsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jarFiles != null) {
                urls = new URL[jarFiles.length];
                for (int i = 0; i < jarFiles.length; i++) {
                    try {
                        urls[i] = jarFiles[i].toURI().toURL();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        URLClassLoader childClassLoader = new URLClassLoader(urls, getClass().getClassLoader());
        return childClassLoader.loadClass(fqn);
    }

    void end() {
    }

}