package com.structurizr.dsl;

final class DeploymentEnvironmentDslContext extends DslContext {

    private String environment;

    DeploymentEnvironmentDslContext(String environment) {
        this.environment = environment;
    }

    String getEnvironment() {
        return environment;
    }

}