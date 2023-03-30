package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;

final class DeploymentNodeParser extends AbstractParser {

    private static final String GRAMMAR = "deploymentNode <name> [description] [technology] [tags] [instances] {";

    private static final int NAME_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int TECHNOLOGY_INDEX = 3;
    private static final int TAGS_INDEX = 4;
    private static final int INSTANCES_INDEX = 5;

    DeploymentNode parse(DeploymentEnvironmentDslContext context, Tokens tokens) {
        // deploymentNode <name> [description] [technology] [tags] [instances]

        if (tokens.hasMoreThan(INSTANCES_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        DeploymentNode deploymentNode = null;
        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        deploymentNode = context.getWorkspace().getModel().addDeploymentNode(context.getEnvironment(), name, description, technology);

        String tags = "";
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX);
            deploymentNode.addTags(tags.split(","));
        }

        String instances = "1";
        if (tokens.includes(INSTANCES_INDEX)) {
            instances = tokens.get(INSTANCES_INDEX);
            deploymentNode.setInstances(instances);
        }

        if (context.hasGroup()) {
            deploymentNode.setGroup(context.getGroup().getName());
            context.getGroup().addElement(deploymentNode);
        }

        return deploymentNode;
    }

    DeploymentNode parse(DeploymentNodeDslContext context, Tokens tokens) {
        // deploymentNode <name> [description] [technology] [tags] [instances]

        if (tokens.hasMoreThan(INSTANCES_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        DeploymentNode deploymentNode = null;
        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        DeploymentNode parent = context.getDeploymentNode();
        deploymentNode = parent.addDeploymentNode(name, description, technology);

        String tags = "";
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX);
            deploymentNode.addTags(tags.split(","));
        }

        String instances = "1";
        if (tokens.includes(INSTANCES_INDEX)) {
            instances = tokens.get(INSTANCES_INDEX);
            deploymentNode.setInstances(instances);
        }

        if (context.hasGroup()) {
            deploymentNode.setGroup(context.getGroup().getName());
            context.getGroup().addElement(deploymentNode);
        }

        return deploymentNode;
    }

    void parseTechnology(DeploymentNodeDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getDeploymentNode().setTechnology(technology);
    }

    void parseInstances(DeploymentNodeDslContext context, Tokens tokens) {
        int index = 1;

        // instances <number|range>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: instances <number|range>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: instances <number|range>");
        }

        String instances = tokens.get(index);
        context.getDeploymentNode().setInstances(instances);
    }

}