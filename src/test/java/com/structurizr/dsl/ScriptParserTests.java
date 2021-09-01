package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ScriptParserTests extends AbstractTests {

    private ScriptParser parser = new ScriptParser();

    @Test
    void test_parseExternal_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseExternal(tokens("!script", "test.kts", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !script <filename>", e.getMessage());
        }
    }

    @Test
    void test_parseExternal_ThrowsAnException_WhenNoFilenameIsSpecified() {
        try {
            parser.parseExternal(tokens("!script"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !script <filename>", e.getMessage());
        }
    }

}