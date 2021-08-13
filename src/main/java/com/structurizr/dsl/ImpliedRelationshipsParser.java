package com.structurizr.dsl;

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.DefaultImpliedRelationshipsStrategy;
import com.structurizr.model.ImpliedRelationshipsStrategy;

import java.util.ArrayList;
import java.util.List;

final class ImpliedRelationshipsParser extends AbstractParser {

    private static final String GRAMMAR = "impliedRelationships <true|false|fqn>";

    private static final int FLAG_INDEX = 1;
    private static final String FALSE = "false";
    private static final String TRUE = "true";

    private boolean restricted = false;

    ImpliedRelationshipsParser(boolean restricted) {
        this.restricted = restricted;
    }

    void parse(DslContext context, Tokens tokens) {
        // impliedRelationships <true|false|fqn>

        if (tokens.hasMoreThan(FLAG_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FLAG_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (tokens.get(FLAG_INDEX).equalsIgnoreCase(FALSE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());
        } else if (tokens.get(FLAG_INDEX).equalsIgnoreCase(TRUE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        } else if (!restricted) {
            String fqn = tokens.get(FLAG_INDEX);
            try {
                ImpliedRelationshipsStrategy strategy = (ImpliedRelationshipsStrategy)Class.forName(fqn).getDeclaredConstructor().newInstance();
                context.getWorkspace().getModel().setImpliedRelationshipsStrategy(strategy);
            } catch (Exception e) {
                throw new RuntimeException("Could not load implied relationships strategy " + fqn);
            }
        }
    }

}