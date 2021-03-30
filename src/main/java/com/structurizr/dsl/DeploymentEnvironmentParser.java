package com.structurizr.dsl;

final class DeploymentEnvironmentParser extends AbstractParser {

    private static final String GRAMMAR = "deploymentEnvironment <name> {";

    private static final int DEPLOYMENT_ENVIRONMENT_NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // deploymentEnvironment <name>

        if (tokens.hasMoreThan(DEPLOYMENT_ENVIRONMENT_NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        } else if (tokens.size() != DEPLOYMENT_ENVIRONMENT_NAME_INDEX + 1) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        } else {
            return tokens.get(DEPLOYMENT_ENVIRONMENT_NAME_INDEX);
        }
    }

}