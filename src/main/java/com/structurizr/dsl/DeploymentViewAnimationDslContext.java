package com.structurizr.dsl;

import com.structurizr.view.DeploymentView;

class DeploymentViewAnimationDslContext extends DslContext {

    private DeploymentView view;

    DeploymentViewAnimationDslContext(DeploymentView view) {
        super();

        this.view = view;
    }

    DeploymentView getView() {
        return view;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ANIMATION_STEP_IN_VIEW_TOKEN
        };
    }

}