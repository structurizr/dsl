package com.structurizr.dsl;

import com.structurizr.configuration.Visibility;

final class ConfigurationParser extends AbstractParser {

    private static final String GRAMMAR = "visibility <private|public>";

    private static final int FIRST_PROPERTY_INDEX = 1;

    private static final String PRIVATE = "private";
    private static final String PUBLIC = "public";

    void parseVisibility(DslContext context, Tokens tokens) {
        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String visibility = tokens.get(1).toLowerCase();

            if (visibility.equalsIgnoreCase(PRIVATE)) {
                context.getWorkspace().getConfiguration().setVisibility(Visibility.Private);
            } else if (visibility.equalsIgnoreCase(PUBLIC)) {
                context.getWorkspace().getConfiguration().setVisibility(Visibility.Public);
            } else {
                throw new RuntimeException("The visibility \"" + visibility + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }
    }

}