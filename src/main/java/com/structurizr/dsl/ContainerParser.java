package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

final class ContainerParser extends AbstractParser {

    private static final String GRAMMAR = "container <name> [description] [technology] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TECHNOLOGY_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    Container parse(SoftwareSystemDslContext context, Tokens tokens) {
        // container <name> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        SoftwareSystem softwareSystem = context.getSoftwareSystem();
        Container container = null;
        String name = tokens.get(NAME_INDEX);

        if (context.isExtendingWorkspace()) {
            container = softwareSystem.getContainerWithName(name);
        }

        if (container == null) {
            container = softwareSystem.addContainer(name);
        }

        if (tokens.includes(DESCRIPTION_INDEX)) {
            String description = tokens.get(DESCRIPTION_INDEX);
            container.setDescription(description);
        }

        if (tokens.includes(TECHNOLOGY_INDEX)) {
            String technology = tokens.get(TECHNOLOGY_INDEX);
            container.setTechnology(technology);
        }

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            container.addTags(tags.split(","));
        }

        if (context.hasGroup()) {
            container.setGroup(context.getGroup().getName());
            context.getGroup().addElement(container);
        }

        return container;
    }

    void parseTechnology(ContainerDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getContainer().setTechnology(technology);
    }

}