package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicViewParserTests extends AbstractTests {

    private DynamicViewParser parser = new DynamicViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("dynamic", "identifier", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: dynamic <*|software system identifier|container identifier> [key] [description] {", e.getMessage());
        }
    }

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

        assertEquals("Dynamic-001", view.getKey());
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
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("person", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

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
        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("dynamic", "softwareSystem"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("Dynamic-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithSoftwareSystemScopeAndKey() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("dynamic", "softwareSystem", "key"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithSoftwareSystemScopeAndKeyAndDescription() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

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
        IdentifiersRegister elements = new IdentifiersRegister();
        Container container = model.addSoftwareSystem("Name", "Description").addContainer("Container", "Description", "Technology");
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("dynamic", "container"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("Dynamic-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(container, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithContainerScopeAndKey() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        Container container = model.addSoftwareSystem("Name", "Description").addContainer("Container", "Description", "Technology");
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("dynamic", "container", "key"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(container, views.get(0).getElement());
    }

    @Test
    void test_parse_CreatesADynamicViewWithContainerScopeAndKeyAndDescription() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        Container container = model.addSoftwareSystem("Name", "Description").addContainer("Container", "Description", "Technology");
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("dynamic", "container", "key", "Description"));
        List<DynamicView> views = new ArrayList<>(this.views.getDynamicViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertSame(container, views.get(0).getElement());
    }

}