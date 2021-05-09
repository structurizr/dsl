package com.structurizr.dsl;

final class DynamicViewParallelSequenceDslContext extends DynamicViewDslContext {

    DynamicViewParallelSequenceDslContext(DynamicViewDslContext context) {
        super(context.getView());
        getView().startParallelSequence();
    }

    @Override
    void end() {
        getView().endParallelSequence();
    }

}