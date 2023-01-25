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

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.BRANDING_LOGO_TOKEN,
                StructurizrDslTokens.BRANDING_FONT_TOKEN
        };
    }

}