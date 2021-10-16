package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentParserTests extends AbstractTests {

    private ComponentParser parser = new ComponentParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new ContainerDslContext(null), tokens("container", "name", "description", "technology", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: component <name> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(new ContainerDslContext(null), tokens("container"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: component <name> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAComponent() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ContainerDslContext context = new ContainerDslContext(container);
        parser.parse(context, tokens("component", "Name"));

        assertEquals(3, model.getElements().size());
        Component component = container.getComponentWithName("Name");
        assertNotNull(component);
        assertEquals("", component.getDescription());
        assertEquals(null, component.getTechnology());
        assertEquals("Element,Component", component.getTags());
    }

    @Test
    void test_parse_CreatesAComponentWithADescription() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ContainerDslContext context = new ContainerDslContext(container);
        parser.parse(context, tokens("component", "Name", "Description"));

        assertEquals(3, model.getElements().size());
        Component component = container.getComponentWithName("Name");
        assertNotNull(component);
        assertEquals("Description", component.getDescription());
        assertEquals(null, component.getTechnology());
        assertEquals("Element,Component", component.getTags());
    }

    @Test
    void test_parse_CreatesAComponentWithADescriptionAndTechnology() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ContainerDslContext context = new ContainerDslContext(container);
        parser.parse(context, tokens("component", "Name", "Description", "Technology"));

        assertEquals(3, model.getElements().size());
        Component component = container.getComponentWithName("Name");
        assertNotNull(component);
        assertEquals("Description", component.getDescription());
        assertEquals("Technology", component.getTechnology());
        assertEquals("Element,Component", component.getTags());
    }

    @Test
    void test_parse_CreatesAComponentWithADescriptionAndTechnologyAndTags() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ContainerDslContext context = new ContainerDslContext(container);
        parser.parse(context, tokens("component", "Name", "Description", "Technology", "Tag 1, Tag 2"));

        assertEquals(3, model.getElements().size());
        Component component = container.getComponentWithName("Name");
        assertNotNull(component);
        assertEquals("Description", component.getDescription());
        assertEquals("Technology", component.getTechnology());
        assertEquals("Element,Component,Tag 1,Tag 2", component.getTags());
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            Component component = model.addSoftwareSystem("Software System").addContainer("Container").addComponent("Component");
            ComponentDslContext context = new ComponentDslContext(component);
            parser.parseTechnology(context, tokens("technology", "technology", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            Component component = model.addSoftwareSystem("Software System").addContainer("Container").addComponent("Component");
            ComponentDslContext context = new ComponentDslContext(component);
            parser.parseTechnology(context, tokens("technology"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_SetsTheDescription_WhenADescriptionIsSpecified() {
        Component component = model.addSoftwareSystem("Software System").addContainer("Container").addComponent("Component");
        ComponentDslContext context = new ComponentDslContext(component);
        parser.parseTechnology(context, tokens("technology", "Technology"));

        assertEquals("Technology", component.getTechnology());
    }

}