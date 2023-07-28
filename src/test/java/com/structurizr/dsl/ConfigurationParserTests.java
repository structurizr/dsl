package com.structurizr.dsl;

import com.structurizr.configuration.Visibility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ConfigurationParserTests extends AbstractTests {

    private final ConfigurationParser parser = new ConfigurationParser();

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