package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.Person;

final class PersonParser extends AbstractParser {

    private static final String GRAMMAR = "person <name> [description] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TAGS_INDEX = 3;

    Person parse(GroupableDslContext context, Tokens tokens) {
        // person <name> [description] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Person person = null;
        String name = tokens.get(NAME_INDEX);

        if (context.isExtendingWorkspace()) {
            person = context.getWorkspace().getModel().getPersonWithName(name);
        }

        if (person == null) {
            person = context.getWorkspace().getModel().addPerson(name);
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
            person.setDescription(description);
        }

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            person.addTags(tags.split(","));
        }

        if (context instanceof EnterpriseDslContext) {
            person.setLocation(Location.Internal);
        }

        if (context.hasGroup()) {
            person.setGroup(context.getGroup().getName());
            context.getGroup().addElement(person);
        }

        return person;
    }

}