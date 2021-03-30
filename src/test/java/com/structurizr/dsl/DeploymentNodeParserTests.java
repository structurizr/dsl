package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentNodeParserTests extends AbstractTests {

    private DeploymentNodeParser parser = new DeploymentNodeParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("deploymentNode", "name", "description", "technology", "tags", "instances", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: deploymentNode <name> [description] [technology] [tags] [instances] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("deploymentNode"));
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
        assertEquals(1, deploymentNode.getInstances());
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
        assertEquals(1, deploymentNode.getInstances());
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
        assertEquals(1, deploymentNode.getInstances());
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
        assertEquals(1, deploymentNode.getInstances());
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
        assertEquals(8, deploymentNode.getInstances());
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
            assertEquals("\"abc\" is not a valid number of instances", e.getMessage());
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
        assertEquals(1, deploymentNode.getInstances());
        assertEquals("Live", deploymentNode.getEnvironment());
    }

}