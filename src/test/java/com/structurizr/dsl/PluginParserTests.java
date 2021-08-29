package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PluginParserTests extends AbstractTests {

    private PluginParser parser = new PluginParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!plugin", "com.example.ClassName", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !plugin <fqn>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoFullyQualifiedNameIsSpecified() {
        try {
            parser.parse(context(), tokens("!plugin"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !plugin <fqn>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenThePluginClassDoesNotExist() {
        try {
            parser.parse(context(), tokens("!plugin", "com.structurizr.TestPlugin"));
            fail();
        } catch (Exception e) {
            assertEquals("Error running plugin com.structurizr.TestPlugin, caused by java.lang.ClassNotFoundException: com.structurizr.TestPlugin", e.getMessage());
        }
    }

    @Test
    void test_parse_RunsThePlugin_WhenAValidPluginIsSpecified() {
        parser.parse(context(), tokens("!plugin", com.structurizr.dsl.TestPlugin.class.getCanonicalName()));
        assertEquals("Name set by plugin", workspace.getName());
    }

}