package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentViewExpressionParserTests extends AbstractTests {

    private DeploymentViewExpressionParser parser = new DeploymentViewExpressionParser();

    @Test
    void test_parseExpression_ThrowsAnException_WhenElementTypeIsNotSupported() {
        try {
            parser.parseExpression("element.type==Component", null);
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element type of \"Component\" is not valid for this view", iae.getMessage());
        }
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsDeploymentNode() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==DeploymentNode", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(deploymentNode));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsInfrastructureNode() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==InfrastructureNode", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(infrastructureNode));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsSoftwareSystem() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==SoftwareSystem", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(softwareSystem));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsSoftwareSystemInstance() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==SoftwareSystemInstance", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(softwareSystemInstance));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsContainer() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsContainerInstance() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==ContainerInstance", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(containerInstance));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementHasTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Infrastructure Node", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(infrastructureNode));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementDoesNotHaveTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag!=Infrastructure Node", context);
        assertEquals(7, elements.size());
        assertFalse(elements.contains(infrastructureNode));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenBooleanAndUsed() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Element && element.type==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenBooleanOrUsed() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode.add(container);

        DeploymentViewDslContext context = new DeploymentViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Software System Instance || element.type==ContainerInstance", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(softwareSystemInstance));
        assertTrue(elements.contains(containerInstance));
    }

}