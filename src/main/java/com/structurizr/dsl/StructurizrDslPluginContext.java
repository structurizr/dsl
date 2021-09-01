package com.structurizr.dsl;

import com.structurizr.Workspace;

import java.util.Map;

public class StructurizrDslPluginContext {

    private Workspace workspace;

    private Map<String,String> parameters;

    StructurizrDslPluginContext(Workspace workspace, Map<String,String> parameters) {
        this.workspace = workspace;
        this.parameters = parameters;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

}