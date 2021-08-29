package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ScriptParserTests extends AbstractTests {

    private ScriptParser parser = new ScriptParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), new File("examples/script.dsl"), tokens("!script", "test.kts", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !script <filename>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoFilenameIsSpecified() {
        try {
            parser.parse(context(), new File("examples/script.dsl"), tokens("!script"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !script <filename>", e.getMessage());
        }
    }

    @Test
    void test_parse_RunsTheScript_WhenAValidScriptFilenameIsSpecified() {
        parser.parse(context(), new File("examples/script.dsl"), tokens("!script", "test.kts"));
        assertNotNull(workspace.getModel().getPersonWithName("Kotlin"));
    }

}