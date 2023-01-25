package com.structurizr.dsl;

import com.structurizr.view.ElementStyle;

import java.io.File;

final class ElementStyleDslContext extends DslContext {

    private File file;
    private ElementStyle style;

    ElementStyleDslContext(ElementStyle style, File file) {
        this.style = style;
        this.file = file;
    }

    File getFile() {
        return file;
    }

    ElementStyle getStyle() {
        return style;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ELEMENT_STYLE_SHAPE_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_ICON_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_WIDTH_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_HEIGHT_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_BACKGROUND_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_COLOR_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_COLOUR_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_STROKE_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_STROKE_WIDTH_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_FONT_SIZE_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_BORDER_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_OPACITY_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_METADATA_TOKEN,
                StructurizrDslTokens.ELEMENT_STYLE_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}