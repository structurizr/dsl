package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.InfrastructureNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfrastructureNodeParserTests extends AbstractTests {

    private InfrastructureNodeParser parser = new InfrastructureNodeParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("infrastructureNode", "name", "description", "technology", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: infrastructureNode <name> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("infrastructureNode"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: infrastructureNode <name> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAnInfrastructureNode() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);

        parser.parse(context, tokens("infrastructureNode", "Name"));

        assertEquals(2, model.getElements().size());
        assertEquals(1, deploymentNode.getInfrastructureNodes().size());
        InfrastructureNode infrastructureNode = deploymentNode.getInfrastructureNodeWithName("Name");
        assertNotNull(infrastructureNode);
        assertEquals("", infrastructureNode.getDescription());
        assertEquals("", infrastructureNode.getTechnology());
        assertEquals("Element,Infrastructure Node", infrastructureNode.getTags());
        assertEquals("Live", infrastructureNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesAnInfrastructureNodeWithADescription() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);

        parser.parse(context, tokens("infrastructureNode", "Name", "Description"));

        assertEquals(2, model.getElements().size());
        assertEquals(1, deploymentNode.getInfrastructureNodes().size());
        InfrastructureNode infrastructureNode = deploymentNode.getInfrastructureNodeWithName("Name");
        assertNotNull(infrastructureNode);
        assertEquals("Description", infrastructureNode.getDescription());
        assertEquals("", infrastructureNode.getTechnology());
        assertEquals("Element,Infrastructure Node", infrastructureNode.getTags());
        assertEquals("Live", infrastructureNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesAnInfrastructureNodeWithADescriptionAndTechnology() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);

        parser.parse(context, tokens("infrastructureNode", "Name", "Description", "Technology"));

        assertEquals(2, model.getElements().size());
        assertEquals(1, deploymentNode.getInfrastructureNodes().size());
        InfrastructureNode infrastructureNode = deploymentNode.getInfrastructureNodeWithName("Name");
        assertNotNull(infrastructureNode);
        assertEquals("Description", infrastructureNode.getDescription());
        assertEquals("Technology", infrastructureNode.getTechnology());
        assertEquals("Element,Infrastructure Node", infrastructureNode.getTags());
        assertEquals("Live", infrastructureNode.getEnvironment());
    }

    @Test
    void test_parse_CreatesAnInfrastructureNodeWithADescriptionAndTechnologyAndTags() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);

        parser.parse(context, tokens("infrastructureNode", "Name", "Description", "Technology", "Tag 1, Tag 2"));

        assertEquals(2, model.getElements().size());
        assertEquals(1, deploymentNode.getInfrastructureNodes().size());
        InfrastructureNode infrastructureNode = deploymentNode.getInfrastructureNodeWithName("Name");
        assertNotNull(infrastructureNode);
        assertEquals("Description", infrastructureNode.getDescription());
        assertEquals("Technology", infrastructureNode.getTechnology());
        assertEquals("Element,Infrastructure Node,Tag 1,Tag 2", infrastructureNode.getTags());
        assertEquals("Live", infrastructureNode.getEnvironment());
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            InfrastructureNode infrastructureNode = model.addDeploymentNode("Deployment Node").addInfrastructureNode("Infrastructure Node");
            InfrastructureNodeDslContext context = new InfrastructureNodeDslContext(infrastructureNode);
            parser.parseTechnology(context, tokens("technology", "technology", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            InfrastructureNode infrastructureNode = model.addDeploymentNode("Deployment Node").addInfrastructureNode("Infrastructure Node");
            InfrastructureNodeDslContext context = new InfrastructureNodeDslContext(infrastructureNode);
            parser.parseTechnology(context, tokens("technology"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_SetsTheDescription_WhenADescriptionIsSpecified() {
        InfrastructureNode infrastructureNode = model.addDeploymentNode("Deployment Node").addInfrastructureNode("Infrastructure Node");
        InfrastructureNodeDslContext context = new InfrastructureNodeDslContext(infrastructureNode);
        parser.parseTechnology(context, tokens("technology", "Technology"));

        assertEquals("Technology", infrastructureNode.getTechnology());
    }

}