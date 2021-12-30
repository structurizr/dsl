package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyParserTests extends AbstractTests {

    @Test
    void test_parseProperty_ThrowsAnException_WhenNoValueIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            PropertiesDslContext context = new PropertiesDslContext(softwareSystem);
            new PropertyParser().parse(context, tokens("name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parseProperty_AddsTheProperty_WhenAValueIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        PropertiesDslContext context = new PropertiesDslContext(softwareSystem);
        new PropertyParser().parse(context, tokens("name", "value"));

        assertEquals("value", softwareSystem.getProperties().get("name"));
    }

}