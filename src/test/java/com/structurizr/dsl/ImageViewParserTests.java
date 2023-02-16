package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageViewParserTests extends AbstractTests {

    private final ImageViewParser parser = new ImageViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("image", "*", "key", "extra"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Too many tokens, expected: image <*|element identifier> [key] {", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("image", "element", "key"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"element\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAnImageView() {
        DslContext context = context();
        parser.parse(context, tokens("image", "*", "key"));

        ImageView imageView = (ImageView)context.getWorkspace().getViews().getViewWithKey("key");
        assertEquals("key", imageView.getKey());
        assertNull(imageView.getElement());
        assertNull(imageView.getElementId());
    }


    @Test
    void test_parse_CreatesAnImageViewForAnElement() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);
        parser.parse(context, tokens("image", "softwaresystem", "key"));

        ImageView imageView = (ImageView)context.getWorkspace().getViews().getViewWithKey("key");
        assertEquals("key", imageView.getKey());
        assertSame(softwareSystem, imageView.getElement());
        assertEquals(softwareSystem.getId(), imageView.getElementId());
    }

}