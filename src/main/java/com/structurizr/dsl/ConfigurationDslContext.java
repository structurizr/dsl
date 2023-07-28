package com.structurizr.dsl;

final class ConfigurationDslContext extends DslContext {

    private static final int FIRST_PROPERTY_INDEX = 1;

    private static final String PRIVATE = "private";
    private static final String PUBLIC = "public";

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.VISIBILITY_TOKEN,
                StructurizrDslTokens.USERS_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}