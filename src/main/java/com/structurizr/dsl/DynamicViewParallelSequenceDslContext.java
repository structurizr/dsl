package com.structurizr.dsl;

final class DynamicViewParallelSequenceDslContext extends DynamicViewDslContext {

    private boolean relationships = false;

    DynamicViewParallelSequenceDslContext(DynamicViewDslContext context) {
        super(context.getView());
        getView().startParallelSequence();
    }

    void hasRelationships(boolean definesRelationships) {
        this.relationships = definesRelationships;
    }

    @Override
    void end() {
        getView().endParallelSequence(!relationships);
    }

}