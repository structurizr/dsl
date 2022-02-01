package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.StringUtils;

import java.util.Map;

public class StructurizrDslPluginContext {

    private Workspace workspace;

    private Map<String,String> parameters;

    public StructurizrDslPluginContext(Workspace workspace, Map<String,String> parameters) {
        this.workspace = workspace;
        this.parameters = parameters;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        String value = parameters.get(name);

        if (StringUtils.isNullOrEmpty(value)) {
            value = defaultValue;
        }

        return value;
    }

}