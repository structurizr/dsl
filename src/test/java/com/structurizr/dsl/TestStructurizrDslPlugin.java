package com.structurizr.dsl;

import com.structurizr.Workspace;

public class TestStructurizrDslPlugin implements StructurizrDslPlugin {

    @Override
    public void run(StructurizrDslPluginContext context) {
        Workspace workspace = context.getWorkspace();
        String name = context.getParameter("name");
        workspace.getModel().addPerson(name);
    }

}