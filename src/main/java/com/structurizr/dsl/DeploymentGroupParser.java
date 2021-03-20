package com.structurizr.dsl;

final class DeploymentGroupParser extends AbstractParser {

    private static final int DEPLOYMENT_GROUP_NAME_INDEX = 1;

    String parse(Tokens tokens) {
        // deploymentGroup <name>

        if (tokens.size() != DEPLOYMENT_GROUP_NAME_INDEX + 1) {
            throw new RuntimeException("Expected: deploymentGroup <name>");
        } else {
            return tokens.get(DEPLOYMENT_GROUP_NAME_INDEX);
        }
    }

}