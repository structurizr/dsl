package com.structurizr.dsl;

final class TerminologyDslContext extends DslContext {

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ENTERPRISE_TOKEN,
                StructurizrDslTokens.PERSON_TOKEN,
                StructurizrDslTokens.SOFTWARE_SYSTEM_TOKEN,
                StructurizrDslTokens.CONTAINER_TOKEN,
                StructurizrDslTokens.COMPONENT_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_NODE_TOKEN,
                StructurizrDslTokens.INFRASTRUCTURE_NODE_TOKEN,
                StructurizrDslTokens.TERMINOLOGY_RELATIONSHIP_TOKEN
        };
    }

}