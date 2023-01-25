package com.structurizr.dsl;

final class WorkspaceDslContext extends DslContext {

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.NAME_TOKEN,
                StructurizrDslTokens.DESCRIPTION_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.DOCS_TOKEN,
                StructurizrDslTokens.ADRS_TOKEN,
                StructurizrDslTokens.IDENTIFIERS_TOKEN,
                StructurizrDslTokens.IMPLIED_RELATIONSHIPS_TOKEN,
                StructurizrDslTokens.MODEL_TOKEN,
                StructurizrDslTokens.VIEWS_TOKEN,
                StructurizrDslTokens.CONFIGURATION_TOKEN
        };
    }

}