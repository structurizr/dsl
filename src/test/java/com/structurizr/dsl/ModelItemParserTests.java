package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelItemParserTests extends AbstractTests {

    private ModelItemParser parser = new ModelItemParser();

    @Test
    void test_parseTags_ThrowsAnException_WhenNoTagsAreSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            ModelItemDslContext context = new SoftwareSystemDslContext(softwareSystem);
            parser.parseTags(context, tokens("tags"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: tags <tags> [tags]", e.getMessage());
        }
    }

    @Test
    void test_parseTags_AddsTheTags_WhenTagsAreSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        ModelItemDslContext context = new SoftwareSystemDslContext(softwareSystem);
        parser.parseTags(context, tokens("tags", "Tag 1"));
        assertEquals(3, softwareSystem.getTagsAsSet().size());
        assertTrue(softwareSystem.getTagsAsSet().contains("Tag 1"));

        parser.parseTags(context, tokens("tags", "Tag 1, Tag 2, Tag 3"));
        assertEquals(5, softwareSystem.getTagsAsSet().size());
        assertTrue(softwareSystem.getTagsAsSet().contains("Tag 2"));
        assertTrue(softwareSystem.getTagsAsSet().contains("Tag 3"));

        parser.parseTags(context, tokens("tags", "Tag 3", "Tag 4", "Tag 5"));
        assertEquals(7, softwareSystem.getTagsAsSet().size());
        assertTrue(softwareSystem.getTagsAsSet().contains("Tag 4"));
        assertTrue(softwareSystem.getTagsAsSet().contains("Tag 5"));
    }

    @Test
    void test_parseUrl_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            ModelItemDslContext context = new SoftwareSystemDslContext(null);
            parser.parseUrl(context, tokens("url", "url", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: url <url>", e.getMessage());
        }
    }

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
    void test_parseProperty_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            ModelItemPropertiesDslContext context = new ModelItemPropertiesDslContext(null);
            parser.parseProperty(context, tokens("name", "value", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <name> <value>", e.getMessage());
        }
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
    void test_parsePerspective_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            ModelItemPerspectivesDslContext context = new ModelItemPerspectivesDslContext(null);
            parser.parsePerspective(context, tokens("name", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <name> <description>", e.getMessage());
        }
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