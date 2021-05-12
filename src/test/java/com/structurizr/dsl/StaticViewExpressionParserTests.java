package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StaticViewExpressionParserTests extends AbstractTests {

    private StaticViewExpressionParser parser = new StaticViewExpressionParser();

    @Test
    void test_parseElementExpression_ThrowsAnException_WhenAnUnsupportedExpressionIsSpecified() {
        try {
            parser.parseElementExpression("element.name==Name", null);
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Unsupported expression \"element.name==Name\"", iae.getMessage());
        }
    }

    @Test
    void test_parseElementExpression_ThrowsAnException_WhenElementTypeIsNotSupported() {
        try {
            parser.parseElementExpression("element.type==DeploymentNode", null);
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element type of \"DeploymentNode\" is not valid for this view", iae.getMessage());
        }
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementTypeEqualsPerson() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.type==Person", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(user));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementTypeEqualsSoftwareSystem() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.type==SoftwareSystem", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(softwareSystem));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementTypeEqualsContainer() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.type==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementTypeEqualsComponent() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.type==Component", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(component));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementHasTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.tag==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenElementDoesNotHaveTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.tag!=Container", context);
        assertEquals(3, elements.size());
        assertFalse(elements.contains(container));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenBooleanAndUsed() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.tag==Element && element.type==Container", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container));
    }

    @Test
    void test_parseElementExpression_ReturnsElements_WhenBooleanOrUsed() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Set<Element> elements = parser.parseElementExpression("element.tag==Container || element.type==Component", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(container));
        assertTrue(elements.contains(component));
    }

}