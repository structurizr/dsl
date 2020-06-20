package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.ComponentView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComponentViewParserTests extends AbstractTests {

    private ComponentViewParser parser = new ComponentViewParser();

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
        Map<String, Element> elements = new HashMap<>();
        elements.put("container", model.addPerson("Name", "Description"));
        context.setElements(elements);

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
        Map<String, Element> elements = new HashMap<>();
        elements.put("container", model.addSoftwareSystem("Name", "Description").addContainer("container", "Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("component", "container"));
        List<ComponentView> views = new ArrayList<>(context.getWorkspace().getViews().getComponentViews());

        assertEquals(1, views.size());
        assertEquals("001-Component", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalContainerBoundariesVisible());
    }

    @Test
    void test_parse_CreatesAComponentViewWithAKey() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("container", model.addSoftwareSystem("Name", "Description").addContainer("container", "Name", "Description"));
        context.setElements(elements);

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
        Map<String, Element> elements = new HashMap<>();
        elements.put("container", model.addSoftwareSystem("Name", "Description").addContainer("container", "Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("component", "container", "key", "Description"));
        List<ComponentView> views = new ArrayList<>(context.getWorkspace().getViews().getComponentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalContainerBoundariesVisible());
    }

}