package com.structurizr.dsl;

import com.structurizr.view.ComponentView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentViewParserTests extends AbstractTests {

    private ComponentViewParser parser = new ComponentViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("component", "container", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: component <container identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheContainerIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("component"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: component <container identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheContainerIsNotDefined() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("component", "container", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The container \"container\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotAContainer() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("component", "container", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"container\" is not a container", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAComponentView() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addSoftwareSystem("Name", "Description").addContainer("Container", "Name", "Description"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("component", "container"));
        List<ComponentView> views = new ArrayList<>(context.getWorkspace().getViews().getComponentViews());

        assertEquals(1, views.size());
        assertEquals("Component-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalContainerBoundariesVisible());
    }

    @Test
    void test_parse_CreatesAComponentViewWithAKey() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addSoftwareSystem("Name", "Description").addContainer("container", "Name", "Description"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("component", "container", "key"));
        List<ComponentView> views = new ArrayList<>(context.getWorkspace().getViews().getComponentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalContainerBoundariesVisible());
    }

    @Test
    void test_parse_CreatesAComponentViewWithAKeyAndDescription() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addSoftwareSystem("Name", "Description").addContainer("container", "Name", "Description"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("component", "container", "key", "Description"));
        List<ComponentView> views = new ArrayList<>(context.getWorkspace().getViews().getComponentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalContainerBoundariesVisible());
    }

}