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

        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = "";
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        SoftwareSystem softwareSystem = context.getSoftwareSystem();
        Container container = softwareSystem.addContainer(name, description, technology);

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            container.addTags(tags.split(","));
        }

        if (context.hasGroup()) {
            container.setGroup(context.getGroup());
        }

        return container;
    }

}