package com.structurizr.dsl;

final class DeploymentGroupParser extends AbstractParser {

    private static final String GRAMMAR = "deploymentGroup <name>";

    private static final int DEPLOYMENT_GROUP_NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // deploymentGroup <name>

        if (tokens.hasMoreThan(DEPLOYMENT_GROUP_NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        } else if (tokens.size() != DEPLOYMENT_GROUP_NAME_INDEX + 1) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        } else {
            return tokens.get(DEPLOYMENT_GROUP_NAME_INDEX);
        }
    }

}