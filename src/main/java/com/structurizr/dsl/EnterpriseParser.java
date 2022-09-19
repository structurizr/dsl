package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Enterprise;

final class EnterpriseParser extends AbstractParser {

    private static final String GRAMMAR = "enterprise [name]";

    private static final int NAME_INDEX = 1;

    void parse(DslContext context, Tokens tokens) {
        Workspace workspace = context.getWorkspace();

        if (tokens.hasMoreThan(NAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        } else if (tokens.includes(NAME_INDEX)) {
            String name = tokens.get(1);

            if (workspace.getModel().getEnterprise() == null) {
                workspace.getModel().setEnterprise(new Enterprise(name));
            } else if (!name.equals(workspace.getModel().getEnterprise().getName())) {
                throw new RuntimeException("The name of the enterprise has already been set");
            }
        } else {
            // do nothing ... this will just create an EnterpriseDslContext, so that people and software systems can be marked as "internal"
        }
    }

}