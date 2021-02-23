package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;

final class PersonParser extends AbstractParser {

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TAGS_INDEX = 3;

    Person parse(GroupableDslContext context, Tokens tokens) {
        // person <name> [description] [tags]

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: person <name> [description] [tags]");
        }

        String name = tokens.get(NAME_INDEX);

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        Person person = getModel(context).addPerson(name, description);

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            person.addTags(tags.split(","));
        }

        if (isInsideEnterpriseContext(context)) {
            person.setLocation(Location.Internal);
        } else if (isOutSideOfEnterpriseContext(context)) {
            person.setLocation(Location.External);
        }

        if (context.hasGroup()) {
            person.setGroup(context.getGroup());
        }

        return person;
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