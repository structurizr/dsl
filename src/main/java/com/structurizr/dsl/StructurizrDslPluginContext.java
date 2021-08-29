package com.structurizr.dsl;

import com.structurizr.Workspace;

public class StructurizrDslPluginContext {

    private Workspace workspace;

    StructurizrDslPluginContext(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

}