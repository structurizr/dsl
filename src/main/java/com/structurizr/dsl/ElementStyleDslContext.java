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

}