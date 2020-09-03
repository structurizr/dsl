package com.structurizr.dsl;

import com.structurizr.configuration.Role;

final class UserRoleParser extends AbstractParser {

    private final static int USERNAME_INDEX = 0;
    private final static int ROLE_INDEX = 1;

    void parse(DslContext context, Tokens tokens) {
        // <username> <read|write>

        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: <username> <read|write>");
        }

        String username = tokens.get(USERNAME_INDEX);
        String roleAsString = tokens.get(ROLE_INDEX);

        Role role;

        if (roleAsString.equalsIgnoreCase("write")) {
            role = Role.ReadWrite;
        } else if (roleAsString.equalsIgnoreCase("read")) {
            role = Role.ReadOnly;
        } else {
            throw new RuntimeException("The role should be \"read\" or \"write\"");
        }

        context.getWorkspace().getConfiguration().addUser(username, role);
    }

}