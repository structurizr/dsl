package com.structurizr.dsl;

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.DefaultImpliedRelationshipsStrategy;

import java.util.ArrayList;
import java.util.List;

final class ImpliedRelationshipsParser extends AbstractParser {

    private static final String GRAMMAR = "!impliedRelationships <true|false>";

    private static final int FLAG_INDEX = 1;
    private static final String FALSE = "false";

    void parse(DslContext context, Tokens tokens) {
        // impliedRelationships <true|false>

        if (tokens.hasMoreThan(FLAG_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FLAG_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (tokens.get(FLAG_INDEX).equalsIgnoreCase(FALSE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());
        } else {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        }
    }

}