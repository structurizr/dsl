package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeploymentEnvironmentParserTests extends AbstractTests {

    private DeploymentEnvironmentParser parser = new DeploymentEnvironmentParser();

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parse(tokens("deploymentEnvironment"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deploymentEnvironment <name> {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(tokens("deploymentEnvironment token1 token2"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deploymentEnvironment <name> {", e.getMessage());
        }
    }

    @Test
    void test_parse() {
        String environment = parser.parse(tokens("deploymentEnvironment", "Live"));
        assertEquals("Live", environment);
    }

}