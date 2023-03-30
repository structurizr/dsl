package com.structurizr.dsl;

import com.structurizr.model.*;

import java.util.HashSet;
import java.util.Set;

final class ContainerInstanceParser extends AbstractParser {

    private static final String GRAMMAR = "containerInstance <identifier> [deploymentGroups] [tags]";

    private static final int IDENTIFIER_INDEX = 1;
    private static final int DEPLOYMENT_GROUPS_TOKEN = 2;
    private static final int TAGS_INDEX = 3;

    ContainerInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // containerInstance <identifier> [deploymentGroups] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String containerIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(containerIdentifier, Container.class);
        if (element == null) {
            throw new RuntimeException("The container \"" + containerIdentifier + "\" does not exist");
        }

        DeploymentNode deploymentNode = context.getDeploymentNode();

        Set<String> deploymentGroups = new HashSet<>();
        if (tokens.includes(DEPLOYMENT_GROUPS_TOKEN)) {
            String token = tokens.get(DEPLOYMENT_GROUPS_TOKEN);

            String[] deploymentGroupReferences = token.split(",");
            for (String deploymentGroupReference : deploymentGroupReferences) {
                Element e = context.getElement(deploymentGroupReference);
                if (e instanceof DeploymentGroup) {
                    deploymentGroups.add(e.getName());
                }
            }
        }

        ContainerInstance containerInstance = deploymentNode.add((Container)element, deploymentGroups.toArray(new String[]{}));

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            containerInstance.addTags(tags.split(","));
        }

        if (context.hasGroup()) {
            containerInstance.setGroup(context.getGroup().getName());
            context.getGroup().addElement(containerInstance);
        }

        return containerInstance;
    }

}