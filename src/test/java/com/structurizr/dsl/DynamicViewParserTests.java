package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DynamicViewParserTests extends AbstractTests {

    private DynamicViewParser parser = new DynamicViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("dynamic"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: dynamic <*|software system identifier|container identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_GeneratesAKey_WhenTheKeyIsMissing() {
        DslContext context = context();
        DynamicView view = parser.parse(context, tokens("dynamic", "*"));

        assertEquals("001-Dynamic", view.getKey());
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotDefined() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("dynamic", "softwareSystem", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The software system or container \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystemOrContainer() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("person", model.addPerson("Name", "Description"));
        context.setElements(elements);

        try {
            parser.parse(context, tokens("dynamic", "person", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"person\" is not a software system or container", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesADynamicViewWithNoScope() {
        parser.parse(context(), tokens("dynamic", "*", "key"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithNoScopeAndADescription() {
        parser.parse(context(), tokens("dynamic", "*", "key", "Description"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertNull(views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithSoftwareSystemScope() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("dynamic", "softwareSystem", "key"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithSoftwareSystemScopeAndADescription() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("dynamic", "softwareSystem", "key", "Description"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithContainerScope() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        Container container = model.addSoftwareSystem("Name", "Description").addContainer("Container", "Description", "Technology");
        elements.put("container", container);
        context.setElements(elements);

        parser.parse(context, tokens("dynamic", "container", "key"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(container, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithContainerScopeAndADescription() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        Container container = model.addSoftwareSystem("Name", "Description").addContainer("Container", "Description", "Technology");
        elements.put("container", container);
        context.setElements(elements);

        parser.parse(context, tokens("dynamic", "container", "key", "Description"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertSame(container, views.get(0).getElement());
    }

}