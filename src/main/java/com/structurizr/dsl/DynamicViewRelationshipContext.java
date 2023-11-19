package com.structurizr.dsl;

import com.structurizr.view.RelationshipView;

class DynamicViewRelationshipContext extends DslContext {

    private final RelationshipView relationshipView;

    DynamicViewRelationshipContext(RelationshipView relationshipView) {
        super();

        this.relationshipView = relationshipView;
    }

    RelationshipView getRelationshipView() {
        return relationshipView;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.URL_TOKEN
        };
    }

}