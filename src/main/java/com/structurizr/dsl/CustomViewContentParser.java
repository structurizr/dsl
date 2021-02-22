package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class CustomViewContentParser extends ViewContentParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;
    private static final int RELATIONSHIP_IDENTIFIER_INDEX = 2;

    private static final String WILDCARD = "*";
    private static final String RELATIONSHIP = "->";

    void parseInclude(CustomViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>");
        }

        CustomView view = context.getCustomView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // include <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                view.add(relationship);
            }
        } else if (tokens.contains(WILDCARD)) {
            // include *
            for (CustomElement element : context.getWorkspace().getModel().getCustomElements()) {
                view.add(element);
            }
        } else {
            // include <identifier> [identifier...]
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String identifier = tokens.get(i);

                Element element = context.getElement(identifier);
                Relationship relationship = context.getRelationship(identifier);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof CustomElement) {
                        view.add((CustomElement) element);
                    } else {
                        throw new RuntimeException("The element \"" + identifier + "\" can not be added to this type of view");
                    }
                }

                if (relationship != null) {
                    view.add(relationship);
                }
            }
        }

    }

    void parseExclude(CustomViewDslContext context, Tokens tokens) {
        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>");
        }

        CustomView view = context.getCustomView();

        if (tokens.size() == 4 && tokens.get(RELATIONSHIP_IDENTIFIER_INDEX).equals(RELATIONSHIP)) {
            // exclude <*|identifier> -> <*|identifier>
            String sourceElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX - 1);
            String destinationElementIdentifier = tokens.get(RELATIONSHIP_IDENTIFIER_INDEX + 1);

            Set<Relationship> relationships = findRelationships(context, sourceElementIdentifier, destinationElementIdentifier);
            for (Relationship relationship : relationships) {
                view.remove(relationship);
            }
        } else {
            // exclude <identifier> [identifier...]
            for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
                String identifier = tokens.get(i);

                Element element = context.getElement(identifier);
                Relationship relationship = context.getRelationship(identifier);
                if (element == null && relationship == null) {
                    throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
                }

                if (element != null) {
                    if (element instanceof CustomElement) {
                        view.remove((CustomElement) element);
                    } else {
                        throw new RuntimeException("The element \"" + identifier + "\" can not be added to this view");
                    }
                }

                if (relationship != null) {
                    view.remove(relationship);
                }
            }
        }
    }

}