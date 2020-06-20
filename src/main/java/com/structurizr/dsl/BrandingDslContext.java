package com.structurizr.dsl;

import java.io.File;

final class BrandingDslContext extends DslContext {

    private File file;

    BrandingDslContext(File file) {
        this.file = file;
    }

    File getFile() {
        return file;
    }

}