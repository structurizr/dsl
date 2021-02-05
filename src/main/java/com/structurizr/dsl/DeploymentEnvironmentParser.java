package com.structurizr.dsl;

final class DeploymentEnvironmentParser extends AbstractParser {

    private static final int DEPLOYMENT_ENVIRONMENT_NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // deploymentEnvironment <name>

        if (tokens.size() != DEPLOYMENT_ENVIRONMENT_NAME_INDEX + 1) {
            throw new RuntimeException("Expected: deploymentEnvironment <name> {");
        } else {
            return tokens.get(DEPLOYMENT_ENVIRONMENT_NAME_INDEX);
        }
    }

}