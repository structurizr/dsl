package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Enterprise;

final class EnterpriseParser extends AbstractParser {

    private static final String GRAMMAR = "enterprise <name>";

    private static final int NAME_INDEX = 1;

    void parse(DslContext context, Tokens tokens) {
        Workspace workspace = context.getWorkspace();

        if (tokens.hasMoreThan(NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        } else if (tokens.includes(NAME_INDEX)) {
            workspace.getModel().setEnterprise(new Enterprise(tokens.get(1)));
        } else {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }
    }

}