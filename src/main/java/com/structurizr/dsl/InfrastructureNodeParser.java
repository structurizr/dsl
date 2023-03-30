package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.InfrastructureNode;

final class InfrastructureNodeParser extends AbstractParser {

    private static final String GRAMMAR = "infrastructureNode <name> [description] [technology] [tags]";

    private static final int NAME_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int TECHNOLOGY_INDEX = 3;
    private static final int TAGS_INDEX = 4;

    InfrastructureNode parse(DeploymentNodeDslContext context, Tokens tokens) {
        // infrastructureNode <name> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
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

        if (context.hasGroup()) {
            infrastructureNode.setGroup(context.getGroup().getName());
            context.getGroup().addElement(infrastructureNode);
        }

        return infrastructureNode;
    }

    void parseTechnology(InfrastructureNodeDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getInfrastructureNode().setTechnology(technology);
    }

}