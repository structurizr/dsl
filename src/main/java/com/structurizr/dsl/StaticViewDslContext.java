package com.structurizr.dsl;

import com.structurizr.view.StaticView;

class StaticViewDslContext extends DslContext {

    private StaticView view;
    private boolean autoAddRelations = true;

    StaticViewDslContext(StaticView view) {
        this.view = view;
    }

    StaticView getView() {
        return view;
    }

    void setAutoAddRelations(boolean autoAddRelations) {
        this.autoAddRelations = autoAddRelations;
    }

    boolean isAutoAddRelations() {
        return this.autoAddRelations;
    }
}