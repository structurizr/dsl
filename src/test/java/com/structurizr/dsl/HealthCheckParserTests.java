package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.HttpHealthCheck;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.SoftwareSystemInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class HealthCheckParserTests extends AbstractTests {

    private HealthCheckParser parser = new HealthCheckParser();

    private SoftwareSystemInstance softwareSystemInstance;

    @BeforeEach
    void setUp() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        softwareSystemInstance = deploymentNode.add(softwareSystem);
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "name", "url", "interval", "timeout", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: healthCheck <name> <url> [interval] [timeout]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoNameIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: healthCheck <name> <url> [interval] [timeout]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoUrlIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "Name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: healthCheck <name> <url> [interval] [timeout]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidIntervalIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health", "hello"));
            fail();
        } catch (Exception e) {
            assertEquals("The interval of \"hello\" is not valid - it must be a positive integer (number of seconds)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenANegativeIntervalIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health", "-1"));
            fail();
        } catch (Exception e) {
            assertEquals("The interval must be a positive integer (number of seconds)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidTimeoutIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health", "60", "hello"));
            fail();
        } catch (Exception e) {
            assertEquals("The timeout of \"hello\" is not valid - it must be zero or a positive integer (number of milliseconds)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenANegativeTimeoutIsSpecified() {
        try {
            SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
            parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health", "60", "-1"));
            fail();
        } catch (Exception e) {
            assertEquals("The timeout must be zero or a positive integer (number of milliseconds)", e.getMessage());
        }
    }

    @Test
    void test_parse_AddsAHealthCheck_WhenTheNameAndUrlAreSpecified() {
        SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
        parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health"));

        HttpHealthCheck healthCheck = softwareSystemInstance.getHealthChecks().iterator().next();
        assertEquals("Name", healthCheck.getName());
        assertEquals("https://example.com/health", healthCheck.getUrl());
        assertEquals(60, healthCheck.getInterval());
        assertEquals(0, healthCheck.getTimeout());
    }

    @Test
    void test_parse_AddsAHealthCheck_WhenAllPropertiesAreSpecified() {
        SoftwareSystemInstanceDslContext context = new SoftwareSystemInstanceDslContext(softwareSystemInstance);
        parser.parse(context, tokens("healthCheck", "Name", "https://example.com/health", "120", "2000"));

        HttpHealthCheck healthCheck = softwareSystemInstance.getHealthChecks().iterator().next();
        assertEquals("Name", healthCheck.getName());
        assertEquals("https://example.com/health", healthCheck.getUrl());
        assertEquals(120, healthCheck.getInterval());
        assertEquals(2000, healthCheck.getTimeout());
    }

}