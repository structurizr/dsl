package com.structurizr.dsl;

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.DefaultImpliedRelationshipsStrategy;

import java.util.ArrayList;
import java.util.List;

final class ImpliedRelationshipsParser extends AbstractParser {

    private static final int FLAG_INDEX = 1;
    private static final String FALSE = "false";

    void parse(DslContext context, Tokens tokens) {
        // impliedRelationships <true|false>
        if (!tokens.includes(FLAG_INDEX)) {
            throw new RuntimeException("Expected: impliedRelationships <true|false>");
        }

        if (tokens.get(FLAG_INDEX).equalsIgnoreCase(FALSE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());
        } else {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        }
    }

}