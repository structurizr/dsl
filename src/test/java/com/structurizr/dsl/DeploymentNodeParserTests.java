package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.model.DeploymentNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentNodeParserTests extends AbstractTests {

    private DeploymentNodeParser parser = new DeploymentNodeParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new DeploymentEnvironmentDslContext("env"), tokens("deploymentNode", "name", "description", "technology", "tags", "instances", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: deploymentNode <name> [description] [technology] [tags] [instances] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(new DeploymentEnvironmentDslContext("env"), tokens("deploymentNode"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deploymentNode <name> [description] [technology] [tags] [instances] {", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesADeploymentNode() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name"));

        assertEquals(1, model.getElements().size());
        DeploymentNode deploymentNode = model.getDeploymentNodeWithName("Name", "Live");
        assertNotNull(deploymentNode);
        assertEquals("", deploymentNode.getDescription());
        assertEquals("", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node", deploymentNode.getTags());
        assertEquals("1", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentNodeWithADescription() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name", "Description"));

        assertEquals(1, model.getElements().size());
        DeploymentNode deploymentNode = model.getDeploymentNodeWithName("Name", "Live");
        assertNotNull(deploymentNode);
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node", deploymentNode.getTags());
        assertEquals("1", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentNodeWithADescriptionAndTechnology() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name", "Description", "Technology"));

        assertEquals(1, model.getElements().size());
        DeploymentNode deploymentNode = model.getDeploymentNodeWithName("Name", "Live");
        assertNotNull(deploymentNode);
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node", deploymentNode.getTags());
        assertEquals("1", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentNodeWithADescriptionAndTechnologyAndTags() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name", "Description", "Technology", "Tag 1, Tag 2"));

        assertEquals(1, model.getElements().size());
        DeploymentNode deploymentNode = model.getDeploymentNodeWithName("Name", "Live");
        assertNotNull(deploymentNode);
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node,Tag 1,Tag 2", deploymentNode.getTags());
        assertEquals("1", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentNodeWithADescriptionAndTechnologyAndTagsAndInstances() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name", "Description", "Technology", "Tag 1, Tag 2", "8"));

        assertEquals(1, model.getElements().size());
        DeploymentNode deploymentNode = model.getDeploymentNodeWithName("Name", "Live");
        assertNotNull(deploymentNode);
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node,Tag 1,Tag 2", deploymentNode.getTags());
        assertEquals("8", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNumberOfInstancesIsNotValid() {
        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("Live");
        context.setWorkspace(workspace);

        try {
            parser.parse(context, tokens("deploymentNode", "Name", "Description", "Technology", "Tag 1, Tag 2", "abc"));
            System.out.println(model.getDeploymentNodes().iterator().next().getInstances());
            fail();
        } catch (Exception e) {
            assertEquals("Number of instances must be a positive integer or a range.", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAChildDeploymentNode() {
        DeploymentNode parent = model.addDeploymentNode("Live", "Parent", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(parent);
        context.setWorkspace(workspace);
        parser.parse(context, tokens("deploymentNode", "Name"));

        assertEquals(2, model.getElements().size());
        DeploymentNode deploymentNode = parent.getDeploymentNodeWithName("Name");
        assertNotNull(deploymentNode);
        assertEquals("", deploymentNode.getDescription());
        assertEquals("", deploymentNode.getTechnology());
        assertEquals("Element,Deployment Node", deploymentNode.getTags());
        assertEquals("1", deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
            DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
            parser.parseTechnology(context, tokens("technology", "technology", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
            DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
            parser.parseTechnology(context, tokens("technology"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_SetsTheTechnology_WhenADescriptionIsSpecified() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        parser.parseTechnology(context, tokens("technology", "Technology"));

        assertEquals("Technology", deploymentNode.getTechnology());
    }

    @Test
    void test_parseInstances_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
            DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
            parser.parseInstances(context, tokens("instances", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: instances <number|range>", e.getMessage());
        }
    }

    @Test
    void test_parseInstances_ThrowsAnException_WhenNoNumberIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
            DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
            parser.parseInstances(context, tokens("instances"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: instances <number|range>", e.getMessage());
        }
    }

    @Test
    void test_parseInstances_ThrowsAnException_WhenAnInvalidNumberIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
            DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
            parser.parseInstances(context, tokens("instances", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Number of instances must be a positive integer or a range.", e.getMessage());
        }
    }

    @Test
    void test_parseInstances_SetsTheInstances_WhenANumberIsSpecified() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        parser.parseInstances(context, tokens("instances", "123"));

        assertEquals("123", deploymentNode.getInstances());
    }

}