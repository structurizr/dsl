package com.structurizr.dsl;

import com.structurizr.Workspace;

public class TestPlugin implements StructurizrDslPlugin {

    @Override
    public void run(StructurizrDslPluginContext context) {
        Workspace workspace = context.getWorkspace();
        workspace.setName("Name set by plugin");
    }

}
