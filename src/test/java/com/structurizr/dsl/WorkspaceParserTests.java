package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkspaceParserTests extends AbstractTests {

    private WorkspaceParser parser = new WorkspaceParser();

    @Test
    void test_parse_DoesNotThrowAnException_WhenNoPropertiesAreSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        parser.parse(workspace, tokens("workspace"));
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

    @Test
    void test_parse_SetsTheWorkspaceName_WhenANameIsSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        parser.parse(workspace, tokens("workspace", "New Name"));
        assertEquals("New Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

    @Test
    void test_parse_SetsTheWorkspaceNameAndDescription_WhenANameAndDescriptionAreSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        parser.parse(workspace, tokens("workspace", "New Name", "New Description"));
        assertEquals("New Name", workspace.getName());
        assertEquals("New Description", workspace.getDescription());
    }

}