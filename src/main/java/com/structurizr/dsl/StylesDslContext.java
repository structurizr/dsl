package com.structurizr.dsl;

final class StylesDslContext extends DslContext {

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ELEMENT_STYLE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_TOKEN
        };
    }

}