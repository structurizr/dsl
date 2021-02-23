package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

final class SoftwareSystemParser extends AbstractParser {

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TAGS_INDEX = 3;

    SoftwareSystem parse(GroupableDslContext context, Tokens tokens) {
        // softwareSystem <name> [description] [tags]

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: softwareSystem <name> [description] [tags]");
        }

        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        SoftwareSystem softwareSystem = context.getWorkspace().getModel().addSoftwareSystem(name, description);

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            softwareSystem.addTags(tags.split(","));
        }

        if (isInsideEnterpriseContext(context)) {
            softwareSystem.setLocation(Location.Internal);
        } else if (isOutSideOfEnterpriseContext(context)) {
            softwareSystem.setLocation(Location.External);
        }

        if (context.hasGroup()) {
            softwareSystem.setGroup(context.getGroup());
        }

        return softwareSystem;
    }

    private boolean isInsideEnterpriseContext(GroupableDslContext context) {
        return context instanceof EnterpriseDslContext;
    }

    private boolean isOutSideOfEnterpriseContext(GroupableDslContext context) {
        return !isInsideEnterpriseContext(context) && getModel(context).getEnterprise() != null;
    }

    private Model getModel(GroupableDslContext context) {
        return context.getWorkspace().getModel();
    }


}