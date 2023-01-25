package com.structurizr.dsl;

final class ViewsDslContext extends DslContext {

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.SYSTEM_LANDSCAPE_VIEW_TOKEN,
                StructurizrDslTokens.SYSTEM_CONTEXT_VIEW_TOKEN,
                StructurizrDslTokens.CONTAINER_VIEW_TOKEN,
                StructurizrDslTokens.COMPONENT_VIEW_TOKEN,
                StructurizrDslTokens.FILTERED_VIEW_TOKEN,
                StructurizrDslTokens.DYNAMIC_VIEW_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_VIEW_TOKEN,
                StructurizrDslTokens.CUSTOM_VIEW_TOKEN,
                StructurizrDslTokens.STYLES_TOKEN,
                StructurizrDslTokens.THEME_TOKEN,
                StructurizrDslTokens.THEMES_TOKEN,
                StructurizrDslTokens.BRANDING_TOKEN,
                StructurizrDslTokens.TERMINOLOGY_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}