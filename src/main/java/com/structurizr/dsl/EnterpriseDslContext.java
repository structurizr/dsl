package com.structurizr.dsl;

final class EnterpriseDslContext extends GroupableDslContext {

    EnterpriseDslContext() {
        super();
    }

    EnterpriseDslContext(ElementGroup group) {
        super(group);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.PERSON_TOKEN,
                StructurizrDslTokens.SOFTWARE_SYSTEM_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}