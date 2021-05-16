package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WorkspaceParserTests extends AbstractTests {

    private WorkspaceParser parser = new WorkspaceParser();

    @Test
    void test_parseTitle_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(null, tokens("workspace", "name", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: workspace [name] [description] or workspace extends <file|url>", e.getMessage());
        }
    }

    @Test
    void test_parse_DoesNotThrowAnException_WhenNoPropertiesAreSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        workspace = parser.parse(null, tokens("workspace"));
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

    @Test
    void test_parse_SetsTheWorkspaceName_WhenANameIsSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        workspace = parser.parse(null, tokens("workspace", "New Name"));
        assertEquals("New Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

    @Test
    void test_parse_SetsTheWorkspaceNameAndDescription_WhenANameAndDescriptionAreSpecified() {
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
        workspace = parser.parse(null, tokens("workspace", "New Name", "New Description"));
        assertEquals("New Name", workspace.getName());
        assertEquals("New Description", workspace.getDescription());
    }

    @Test
    void test_parseName_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            ModelItemDslContext context = new SoftwareSystemDslContext(null);
            parser.parseName(context, tokens("name", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: name <name>", e.getMessage());
        }
    }

    @Test
    void test_parseName_ThrowsAnException_WhenNoUrlIsSpecified() {
        try {
            parser.parseName(context(), tokens("name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: name <name>", e.getMessage());
        }
    }

    @Test
    void test_parseName_SetsTheName_WhenANameIsSpecified() {
        parser.parseName(context(), tokens("name", "A new name"));

        assertEquals("A new name", context().getWorkspace().getName());
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            ModelItemDslContext context = new SoftwareSystemDslContext(null);
            parser.parseDescription(context, tokens("description", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: description <description>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenNoUrlIsSpecified() {
        try {
            parser.parseDescription(context(), tokens("description"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: description <description>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_SetsTheDescription_WhenADescriptionIsSpecified() {
        parser.parseDescription(context(), tokens("description", "A new description"));

        assertEquals("A new description", context().getWorkspace().getDescription());
    }

}