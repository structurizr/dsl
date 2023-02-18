package com.structurizr.dsl;

import com.structurizr.view.FilteredView;

class FilteredViewDslContext extends ViewDslContext {

    FilteredViewDslContext(FilteredView view) {
        super(view);
    }

    FilteredView getView() {
        return (FilteredView)super.getView();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.DEFAULT_VIEW_TOKEN,
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}