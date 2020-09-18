package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;

final class RelationshipDslContext extends ModelItemDslContext {

    private Relationship relationship;

    RelationshipDslContext(Relationship relationship) {
        this.relationship = relationship;
    }

    Relationship getRelationship() {
        return relationship;
    }

    @Override
    ModelItem getModelItem() {
        return getRelationship();
    }

}