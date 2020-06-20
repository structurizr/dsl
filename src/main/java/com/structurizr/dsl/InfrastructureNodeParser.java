package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.InfrastructureNode;

final class InfrastructureNodeParser extends AbstractParser {

    private static final int NAME_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int TECHNOLOGY_INDEX = 3;
    private static final int TAGS_INDEX = 4;

    InfrastructureNode parse(DeploymentNodeDslContext context, Tokens tokens) {
        // infrastructureNode <name> [description] [technology] [tags]

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: infrastructureNode <name> [description] [technology] [tags]");
        }

        DeploymentNode deploymentNode = context.getDeploymentNode();
        InfrastructureNode infrastructureNode;
        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        infrastructureNode = deploymentNode.addInfrastructureNode(name, description, technology);

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            infrastructureNode.addTags(tags.split(","));
        }

        return infrastructureNode;
    }

}