package com.structurizr.dsl;

import com.structurizr.view.SystemContextView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SystemContextViewParserTests extends AbstractTests {

    private SystemContextViewParser parser = new SystemContextViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("systemContext", "identifier", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: systemContext <software system identifier> [key] [description] {", e.getMessage());
        }
    }

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
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

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
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("systemContext", "softwareSystem"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("SystemContext-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemContextViewWithAKey() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("systemContext", "softwareSystem", "key"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemContextViewWithAKeyAndDescription() {
        DslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setIdentifierRegister(register);

        parser.parse(context, tokens("systemContext", "softwareSystem", "key", "Description"));
        List<SystemContextView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemContextViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
    }

}