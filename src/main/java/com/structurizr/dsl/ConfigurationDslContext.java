package com.structurizr.dsl;

final class ConfigurationDslContext extends DslContext {

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.SCOPE_TOKEN,
                StructurizrDslTokens.VISIBILITY_TOKEN,
                StructurizrDslTokens.USERS_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}