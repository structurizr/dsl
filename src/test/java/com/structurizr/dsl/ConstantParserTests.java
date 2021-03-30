package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ConstantParserTests extends AbstractTests {

    private ConstantParser parser = new ConstantParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!constant", "name", "value", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !constant <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoNameOrValueIsSpecified() {
        try {
            parser.parse(context(), tokens("!constant"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !constant <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoValueIsSpecified() {
        try {
            parser.parse(context(), tokens("!constant", "name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !constant <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNameContainsDisallowedCharacters() {
        try {
            parser.parse(context(), tokens("!constant", "${NAME}", "value"));
            fail();
        } catch (Exception e) {
            assertEquals("Constant names must only contain the following characters: a-zA-Z0-9-_.", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAConstant() {
        Constant constant = parser.parse(context(), tokens("!constant", "name", "value"));
        assertEquals("name", constant.getName());
        assertEquals("value", constant.getValue());
    }

}