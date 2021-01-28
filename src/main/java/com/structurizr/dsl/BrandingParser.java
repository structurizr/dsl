package com.structurizr.dsl;

import com.structurizr.util.ImageUtils;
import com.structurizr.view.Font;

import java.io.File;

final class BrandingParser extends AbstractParser {

    private static final int LOGO_FILE_INDEX = 1;

    private static final int FONT_NAME_INDEX = 1;
    private static final int FONT_URL_INDEX = 2;

    void parseLogo(BrandingDslContext context, Tokens tokens) {
        // logo <path>

        if (tokens.includes(LOGO_FILE_INDEX)) {
            String path = tokens.get(1);

            File file = new File(context.getFile().getParent(), path);
            if (file.exists() && !file.isDirectory()) {
                try {
                    String dataUri = ImageUtils.getImageAsDataUri(file);
                    context.getWorkspace().getViews().getConfiguration().getBranding().setLogo(dataUri);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(path + " does not exist");
            }
        } else {
            throw new RuntimeException("Expected: logo <path>");
        }
    }

    void parseFont(BrandingDslContext context, Tokens tokens) {
        // font <name> [url]

        if (tokens.includes(FONT_URL_INDEX)) {
            String name = tokens.get(FONT_NAME_INDEX);
            String url = tokens.get(FONT_URL_INDEX);

            context.getWorkspace().getViews().getConfiguration().getBranding().setFont(new Font(name, url));
        } else if (tokens.includes(FONT_NAME_INDEX)) {
            String name = tokens.get(FONT_NAME_INDEX);

            context.getWorkspace().getViews().getConfiguration().getBranding().setFont(new Font(name));
        } else {
            throw new RuntimeException("Expected: font <name> [url]");
        }
    }

}