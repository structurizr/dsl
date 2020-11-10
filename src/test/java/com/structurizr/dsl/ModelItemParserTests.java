package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ModelItemParserTests extends AbstractTests {

    private ModelItemParser parser = new ModelItemParser();

    @Test
    void test_parseUrl_ThrowsAnException_WhenNoUrlIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            ModelItemDslContext context = new SoftwareSystemDslContext(softwareSystem);
            parser.parseUrl(context, tokens("url"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: url <url>", e.getMessage());
        }
    }

    @Test
    void test_parseUrl_SetsTheUrl_WhenAUrlIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        ModelItemDslContext context = new SoftwareSystemDslContext(softwareSystem);
        parser.parseUrl(context, tokens("url", "http://example.com"));

        assertEquals("http://example.com", softwareSystem.getUrl());
    }

    @Test
    void test_parseProperty_ThrowsAnException_WhenNoValueIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            ModelItemPropertiesDslContext context = new ModelItemPropertiesDslContext(softwareSystem);
            parser.parseProperty(context, tokens("name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parseProperty_AddsTheProperty_WhenAValueIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        ModelItemPropertiesDslContext context = new ModelItemPropertiesDslContext(softwareSystem);
        parser.parseProperty(context, tokens("name", "value"));

        assertEquals("value", softwareSystem.getProperties().get("name"));
    }

    @Test
    void test_parsePerspective_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            ModelItemPerspectivesDslContext context = new ModelItemPerspectivesDslContext(softwareSystem);
            parser.parsePerspective(context, tokens("name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <name> <description>", e.getMessage());
        }
    }

    @Test
    void test_parsePerspective_AddsThePerspective_WhenAValueIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        ModelItemPerspectivesDslContext context = new ModelItemPerspectivesDslContext(softwareSystem);
        parser.parsePerspective(context, tokens("Security", "Description"));

        assertEquals("Description", softwareSystem.getPerspectives().stream().filter(p -> p.getName().equals("Security")).findFirst().get().getDescription());
    }

}