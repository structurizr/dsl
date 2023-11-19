package com.structurizr.dsl;

import com.structurizr.configuration.Visibility;
import com.structurizr.configuration.WorkspaceScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ConfigurationParserTests extends AbstractTests {

    private final ConfigurationParser parser = new ConfigurationParser();

    @Test
    void test_parseScope_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseScope(context(), tokens("scope", "landscape", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: scope <landscape|softwaresystem|none>", e.getMessage());
        }
    }

    @Test
    void test_parseScope_ThrowsAnException_WhenTheScopeIsMissing() {
        try {
            parser.parseScope(context(), tokens("scope"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: scope <landscape|softwaresystem|none>", e.getMessage());
        }
    }

    @Test
    void test_parseScope_ThrowsAnException_WhenTheScopeIsNotValid() {
        try {
            parser.parseScope(context(), tokens("scope", "container"));
            fail();
        } catch (Exception e) {
            assertEquals("The scope \"container\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseScope_SetsTheScope() {
        parser.parseScope(context(), tokens("scope", "softwaresystem"));
        assertEquals(WorkspaceScope.SoftwareSystem, workspace.getConfiguration().getScope());
    }

    @Test
    void test_parseVisibility_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseVisibility(context(), tokens("visibility", "public", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: visibility <private|public>", e.getMessage());
        }
    }

    @Test
    void test_parseVisibility_ThrowsAnException_WhenTheVisibilityIsMissing() {
        try {
            parser.parseVisibility(context(), tokens("visibility"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: visibility <private|public>", e.getMessage());
        }
    }

    @Test
    void test_parseVisibility_ThrowsAnException_WhenTheVisibilityIsNotValid() {
        try {
            parser.parseVisibility(context(), tokens("visibility", "shared"));
            fail();
        } catch (Exception e) {
            assertEquals("The visibility \"shared\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseVisibility_SetsTheVisibility() {
        parser.parseVisibility(context(), tokens("visibility", "public"));
        assertEquals(Visibility.Public, workspace.getConfiguration().getVisibility());
    }

}