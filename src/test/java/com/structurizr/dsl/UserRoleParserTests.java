package com.structurizr.dsl;

import com.structurizr.configuration.Role;
import com.structurizr.configuration.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UserRoleParserTests extends AbstractTests {

    private UserRoleParser parser = new UserRoleParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("username", "role", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <username> <read|write>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoUsernameIsSpecified() {
        try {
            parser.parse(context(), tokens(""));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <username> <read|write>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoRoleIsSpecified() {
        try {
            parser.parse(context(), tokens("user@example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <username> <read|write>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidRoleIsSpecified() {
        try {
            parser.parse(context(), tokens("user@example.com", "role"));
            fail();
        } catch (Exception e) {
            assertEquals("The role should be \"read\" or \"write\"", e.getMessage());
        }
    }

    @Test
    void test_parse_AddsAReadOnlyUser() {
        parser.parse(context(), tokens("user@example.com", "read"));

        Set<User> users = context().getWorkspace().getConfiguration().getUsers();
        assertEquals(1, users.size());

        User user = users.stream().findFirst().get();
        assertEquals("user@example.com", user.getUsername());
        assertEquals(Role.ReadOnly, user.getRole());
    }

    @Test
    void test_parse_AddsAReadWriteUser() {
        parser.parse(context(), tokens("user@example.com", "write"));

        Set<User> users = context().getWorkspace().getConfiguration().getUsers();
        assertEquals(1, users.size());

        User user = users.stream().findFirst().get();
        assertEquals("user@example.com", user.getUsername());
        assertEquals(Role.ReadWrite, user.getRole());
    }

}