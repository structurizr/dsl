package com.structurizr.dsl;

import com.structurizr.view.DeploymentView;

final class DeploymentViewDslContext extends ModelViewDslContext {

    DeploymentViewDslContext(DeploymentView view) {
        super(view);
    }

    DeploymentView getView() {
        return (DeploymentView)super.getView();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.INCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.EXCLUDE_IN_VIEW_TOKEN,
                StructurizrDslTokens.AUTOLAYOUT_VIEW_TOKEN,
                StructurizrDslTokens.DEFAULT_VIEW_TOKEN,
                StructurizrDslTokens.ANIMATION_IN_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}