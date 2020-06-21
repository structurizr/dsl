package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.SystemContextView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SystemContextViewParserTests extends AbstractTests {

    private SystemContextViewParser parser = new SystemContextViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenTheSoftwareSystemIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("systemContext"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: systemContext <software system identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSoftwareSystemIsNotDefined() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("systemContext", "softwareSystem", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The software system \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystem() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addPerson("Name", "Description"));
        context.setElements(elements);

        try {
            parser.parse(context, tokens("systemContext", "softwareSystem", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" is not a software system", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASystemContextView() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("systemContext", "softwareSystem"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("Name-SystemContext", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemContextViewWithAKey() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("systemContext", "softwareSystem", "key"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemContextViewWithAKeyAndDescription() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("systemContext", "softwareSystem", "key", "Description"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
    }

}