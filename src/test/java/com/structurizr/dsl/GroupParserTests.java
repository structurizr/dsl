package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupParserTests extends AbstractTests {

    private GroupParser parser = new GroupParser();

    @Test
    void parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(null, tokens("group", "name", "{", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parse_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parse(null, tokens("group"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parse_ThrowsAnException_WhenTheBraceIsMissing() {
        try {
            parser.parse(null, tokens("group", "Name", "foo"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parse() {
        ElementGroup group = parser.parse(context(), tokens("group", "Group 1", "{"));
        assertEquals("Group 1", group.getName());
        assertTrue(group.getElements().isEmpty());
    }

    @Test
    void parse_NestedGroup_ThrowsAnExceptionWhenNestedGroupsAreNotConfigured() {
        ModelDslContext context = new ModelDslContext(new ElementGroup(workspace.getModel(), "Group 1"));
        context.setWorkspace(workspace);

        try {
            parser.parse(context, tokens("group", "Group 2", "{"));
            fail();
        } catch (Exception e) {
            assertEquals("To use nested groups, please define a model property named structurizr.groupSeparator", e.getMessage());
        }
    }

    @Test
    void parse_NestedGroup() {
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        ModelDslContext context = new ModelDslContext(new ElementGroup(workspace.getModel(), "Group 1"));
        context.setWorkspace(workspace);

        ElementGroup group = parser.parse(context, tokens("group", "Group 2", "{"));
        assertEquals("Group 1/Group 2", group.getName());
        assertTrue(group.getElements().isEmpty());
    }

}