package com.structurizr.dsl;

import com.structurizr.view.DeploymentView;

final class DeploymentViewDslContext extends ViewDslContext {

    private DeploymentView view;

    DeploymentViewDslContext(DeploymentView view) {
        super(view);

        this.view = view;
    }

    DeploymentView getView() {
        return view;
    }

}