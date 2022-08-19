package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerInstanceParserTests extends AbstractTests {

    private ContainerInstanceParser parser = new ContainerInstanceParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("containerInstance", "identifier", "deploymentGroups", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: containerInstance <identifier> [deploymentGroups] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierIsNotSpecified() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("containerInstance"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: containerInstance <identifier> [deploymentGroups] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("containerInstance", "container"));
            fail();
        } catch (Exception e) {
            assertEquals("The container \"container\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotAContainer() {
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(null);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("containerInstance", "container"));
            fail();
        } catch (Exception e) {
            assertEquals("The container \"container\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAContainerInstanceInTheDefaultDeploymentGroup() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("containerInstance", "container"));

        assertEquals(4, model.getElements().size());
        assertEquals(1, deploymentNode.getContainerInstances().size());
        ContainerInstance containerInstance = deploymentNode.getContainerInstances().iterator().next();
        assertSame(container, containerInstance.getContainer());
        assertEquals("Container Instance", containerInstance.getTags());
        assertEquals("Live", containerInstance.getEnvironment());
        assertEquals(1, containerInstance.getDeploymentGroups().size());
        assertEquals("Default", containerInstance.getDeploymentGroups().iterator().next());
    }

    @Test
    void test_parse_CreatesAContainerInstanceInTheDefaultDeploymentGroupWithTags() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("containerInstance", "container", "", "Tag 1, Tag 2"));

        assertEquals(4, model.getElements().size());
        assertEquals(1, deploymentNode.getContainerInstances().size());
        ContainerInstance containerInstance = deploymentNode.getContainerInstances().iterator().next();
        assertSame(container, containerInstance.getContainer());
        assertEquals("Container Instance,Tag 1,Tag 2", containerInstance.getTags());
        assertEquals("Live", containerInstance.getEnvironment());
        assertEquals(1, containerInstance.getDeploymentGroups().size());
        assertEquals("Default", containerInstance.getDeploymentGroups().iterator().next());
    }

    @Test
    void test_parse_CreatesAContainerInstanceInTheSpecifiedDeploymentGroup() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        elements.register("group", new DeploymentGroup("Group"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("containerInstance", "container", "group"));

        assertEquals(4, model.getElements().size());
        assertEquals(1, deploymentNode.getContainerInstances().size());
        ContainerInstance containerInstance = deploymentNode.getContainerInstances().iterator().next();
        assertSame(container, containerInstance.getContainer());
        assertEquals("Container Instance", containerInstance.getTags());
        assertEquals("Live", containerInstance.getEnvironment());
        assertEquals(1, containerInstance.getDeploymentGroups().size());
        assertEquals("Group", containerInstance.getDeploymentGroups().iterator().next());
    }

    @Test
    void test_parse_CreatesAContainerInstanceInTheSpecifiedDeploymentGroupWithTags() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        elements.register("group", new DeploymentGroup("Group"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("containerInstance", "container", "group", "Tag 1, Tag 2"));

        assertEquals(4, model.getElements().size());
        assertEquals(1, deploymentNode.getContainerInstances().size());
        ContainerInstance containerInstance = deploymentNode.getContainerInstances().iterator().next();
        assertSame(container, containerInstance.getContainer());
        assertEquals("Container Instance,Tag 1,Tag 2", containerInstance.getTags());
        assertEquals("Live", containerInstance.getEnvironment());
        assertEquals(1, containerInstance.getDeploymentGroups().size());
        assertEquals("Group", containerInstance.getDeploymentGroups().iterator().next());
    }

}