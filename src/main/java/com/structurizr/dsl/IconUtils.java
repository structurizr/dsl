package com.structurizr.dsl;

import com.structurizr.util.Url;

class IconUtils {

    public static boolean isSupported(String url) {
        url = url.trim();

        if (Url.isUrl(url) && isSupportedUrl(url)) {
            // all good
            return true;
        }

        if (url.startsWith("data:image")) {
            if (isSupportedDataUri(url)) {
                // all good
                return true;
            } else {
                // it's a data URI, but not supported
                return false;
            }
        }

        return false;
    }

    private static boolean isSupportedDataUri(String uri) {
        return uri.startsWith("data:image/png;base64,") || uri.startsWith("data:image/jpeg;base64,");
    }

    private static boolean isSupportedUrl(String url) {
        url = url.toLowerCase();

        return url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg");
    }

}