package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.SoftwareSystem;

final class SoftwareSystemParser extends AbstractParser {

    private static final String GRAMMAR = "softwareSystem <name> [description] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TAGS_INDEX = 3;

    SoftwareSystem parse(GroupableDslContext context, Tokens tokens) {
        // softwareSystem <name> [description] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        SoftwareSystem softwareSystem = null;
        String name = tokens.get(NAME_INDEX);

        if (context.isExtendingWorkspace()) {
            softwareSystem = context.getWorkspace().getModel().getSoftwareSystemWithName(name);
        }

        if (softwareSystem == null) {
            softwareSystem = context.getWorkspace().getModel().addSoftwareSystem(name);
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
            softwareSystem.setDescription(description);
        }

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            softwareSystem.addTags(tags.split(","));
        }

        if (context instanceof EnterpriseDslContext) {
            softwareSystem.setLocation(Location.Internal);
        }

        if (context.hasGroup()) {
            softwareSystem.setGroup(context.getGroup().getName());
            context.getGroup().addElement(softwareSystem);
        }

        return softwareSystem;
    }

}