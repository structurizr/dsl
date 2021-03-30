package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomElementParserTests extends AbstractTests {

    private CustomElementParser parser = new CustomElementParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("element", "name", "metadata", "description", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: element <name> [metadata] [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("element"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: element <name> [metadata] [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesACustomElement() {
        parser.parse(context(), tokens("element", "Name"));

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadata() {
        parser.parse(context(), tokens("element", "Name", "Box"));

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadataAndDescription() {
        parser.parse(context(), tokens("element", "Name", "Box", "Description"));

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("Description", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadataAndDescriptionAndTags() {
        parser.parse(context(), tokens("element", "Name", "Box", "Description", "Tag 1, Tag 2"));

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("Description", element.getDescription());
        assertEquals("Element,Tag 1,Tag 2", element.getTags());
    }

}