package com.structurizr.dsl;

import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ImageView;

import java.io.IOException;

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

    @Override
    void end() {
        super.end();

        // try to set the content type if it hasn't been set ... this helps the diagram render with image sizing/scaling
        ImageView imageView = getView();
        if (StringUtils.isNullOrEmpty(imageView.getContentType())) {
            if (ImageUtils.isSupportedDataUri(imageView.getContent())) {
                imageView.setContentType(ImageUtils.getContentTypeFromDataUri(imageView.getContent()));
            } else {
                try {
                    imageView.setContentType(ImageUtils.getContentType(imageView.getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                    // ignore
                }
            }
        }
    }
}