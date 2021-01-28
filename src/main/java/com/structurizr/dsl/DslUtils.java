package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DslUtils {

    private static final String STRUCTURIZR_DSL_PROPERTY_NAME = "structurizr.dsl";

    public static String getDsl(Workspace workspace) {
        String base64 = workspace.getProperties().get(STRUCTURIZR_DSL_PROPERTY_NAME);
        String dsl = "";

        if (!StringUtils.isNullOrEmpty(base64)) {
            dsl = new String(Base64.getDecoder().decode(base64));
        }

        return dsl;
    }

    static void setDsl(Workspace workspace, String dsl) {
        String base64 = "";
        if (!StringUtils.isNullOrEmpty(dsl)) {
            base64 = Base64.getEncoder().encodeToString(dsl.getBytes(StandardCharsets.UTF_8));
        }

        workspace.addProperty(STRUCTURIZR_DSL_PROPERTY_NAME, base64);
    }

}