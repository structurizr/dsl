package com.structurizr.dsl;

import com.structurizr.view.ImageView;

class ImageViewDslContext extends ViewDslContext {

    ImageViewDslContext(ImageView view) {
        super(view);
    }

    ImageView getView() {
        return (ImageView)super.getView();
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PLANTUML_TOKEN,
                StructurizrDslTokens.MERMAID_TOKEN,
                StructurizrDslTokens.KROKI_TOKEN,
                StructurizrDslTokens.IMAGE_TOKEN
        };
    }

}