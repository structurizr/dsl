package com.structurizr.dsl;

final class DeploymentEnvironmentDslContext extends GroupableDslContext {

    private final String environment;

    DeploymentEnvironmentDslContext(String environment) {
        super(null);
        this.environment = environment;
    }

    DeploymentEnvironmentDslContext(String environment, ElementGroup group) {
        super(group);
        this.environment = environment;
    }

    String getEnvironment() {
        return environment;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_GROUP_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_NODE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}