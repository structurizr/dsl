package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupParserTests extends AbstractTests {

    private GroupParser parser = new GroupParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(tokens("group", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parse(tokens("group"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void test_parse() {
        ElementGroup group = parser.parse(tokens("group", "Group 1"));
        assertEquals("Group 1", group.getName());
        assertTrue(group.getElements().isEmpty());
    }

}