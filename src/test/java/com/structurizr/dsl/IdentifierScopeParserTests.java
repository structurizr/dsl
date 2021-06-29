package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IdentifierScopeParserTests extends AbstractTests {

    private IdentifierScopeParser parser = new IdentifierScopeParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!identifiers", "hierarchical", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !identifiers <flat|hierarchical>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoScopeIsSpecified() {
        try {
            parser.parse(context(), tokens("!identifiers"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !identifiers <flat|hierarchical>", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheScope_WhenLocalIsSpecified() {
        IdentifierScope scope = parser.parse(context(), tokens("!identifiers", "hierarchical"));

        assertEquals(IdentifierScope.Hierarchical, scope);
    }

    @Test
    void test_parse_SetsTheScope_WhenGlobalIsSpecified() {
        IdentifierScope scope = parser.parse(context(), tokens("!identifiers", "flat"));

        assertEquals(IdentifierScope.Flat, scope);
    }

}