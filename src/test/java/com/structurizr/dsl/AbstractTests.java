package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

import java.util.ArrayList;
import java.util.Arrays;

abstract class AbstractTests {

    protected Workspace workspace = new Workspace("Name", "Description");
    protected Model model = workspace.getModel();
    protected ViewSet views = workspace.getViews();

    protected ModelDslContext context() {
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        ModelDslContext context = new ModelDslContext();
        context.setWorkspace(workspace);

        return context;
    }

    protected Tokens tokens(String... tokens) {
        return new Tokens(new ArrayList<>(Arrays.asList(tokens)));
    }

}