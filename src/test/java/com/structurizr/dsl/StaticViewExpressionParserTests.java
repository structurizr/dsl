package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StaticViewExpressionParserTests extends AbstractTests {

    private StaticViewExpressionParser parser = new StaticViewExpressionParser();

    @Test
    void test_parseExpression_ThrowsAnException_WhenElementTypeIsNotSupported() {
        try {
            parser.parseExpression("element.type==DeploymentNode", null);
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element type of \"DeploymentNode\" is not valid for this view", iae.getMessage());
        }
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsPerson() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==Person", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(user));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsSoftwareSystem() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==SoftwareSystem", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(softwareSystem));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsContainer() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementTypeEqualsComponent() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.type==Component", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(component));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementHasTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenElementDoesNotHaveTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag!=Container", context);
        assertEquals(3, elements.size());
        assertFalse(elements.contains(container));
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

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Container || element.type==Component", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(container));
        assertTrue(elements.contains(component));
    }

    @Test
    void test_parseExpression_ReturnsElements_ForAfferentCouplings() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        component1.uses(component2, "Uses");

        ComponentViewDslContext context = new ComponentViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("container1", container1);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("container1->", context);
        assertEquals(4, elements.size());
        assertTrue(elements.contains(container1));
        assertTrue(elements.contains(softwareSystem2));
        assertTrue(elements.contains(container2));
        assertTrue(elements.contains(component2));
    }

    @Test
    void test_parseExpression_ReturnsElements_ForAfferentCouplingsOfType() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        component1.uses(component2, "Uses");

        ComponentViewDslContext context = new ComponentViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("container1", container1);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("container1-> && element.type==Container", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(container1));
        assertTrue(elements.contains(container2));
    }

}