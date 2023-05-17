package com.structurizr.dsl;

import javax.annotation.Nonnull;
import java.io.File;

final class BrandingDslContext extends DslContext {

    @Nonnull
    private final File file;

    BrandingDslContext(@Nonnull File file) {
        this.file = file;
    }

    @Nonnull
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
