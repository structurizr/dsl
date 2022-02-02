package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IncludeParserTests extends AbstractTests {

    private IncludeParser parser = new IncludeParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new IncludedDslContext(null), tokens("!include", "file", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !include <file|directory|url>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenAFileIsNotSpecified() {
        try {
            parser.parse(new IncludedDslContext(null), tokens("!include"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !include <file|directory|url>", e.getMessage());
        }
    }

}