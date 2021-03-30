package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnterpriseParserTests extends AbstractTests {

    private EnterpriseParser parser = new EnterpriseParser();

    @Test
    void test_parse_SetsTheEnterpriseName() {
        assertNull(workspace.getModel().getEnterprise());
        parser.parse(context(), tokens("enterprise", "New Name"));
        assertEquals("New Name", workspace.getModel().getEnterprise().getName());
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("enterprise", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: enterprise <name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoNameIsSpecified() {
        try {
            parser.parse(context(), tokens("enterprise"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: enterprise <name>", e.getMessage());
        }
    }

}