package com.structurizr.dsl;

import com.structurizr.view.DeploymentView;

final class DeploymentViewDslContext extends DslContext {

    private DeploymentView view;

    DeploymentViewDslContext(DeploymentView view) {
        this.view = view;
    }

    DeploymentView getView() {
        return view;
    }

}