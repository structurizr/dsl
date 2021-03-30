package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeploymentGroupParserTests extends AbstractTests {

    private DeploymentGroupParser parser = new DeploymentGroupParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(tokens("deploymentGroup", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: deploymentGroup <name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parse(tokens("deploymentGroup"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deploymentGroup <name>", e.getMessage());
        }
    }

    @Test
    void test_parse() {
        String service1 = parser.parse(tokens("deploymentGroup", "Service 1"));
        assertEquals("Service 1", service1);
    }

}