package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoftwareSystemParserTests extends AbstractTests {

    private SoftwareSystemParser parser = new SoftwareSystemParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("softwareSystem", "name", "description", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: softwareSystem <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: softwareSystem <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASoftwareSystem() {
        parser.parse(context(), tokens("softwareSystem", "Name"));

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("", softwareSystem.getDescription());
        assertEquals(Location.Unspecified, softwareSystem.getLocation());
        assertEquals("Element,Software System", softwareSystem.getTags());
    }

    @Test
    void test_parse_CreatesASoftwareSystemWithADescription() {
        parser.parse(context(), tokens("softwareSystem", "Name", "Description"));

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("Description", softwareSystem.getDescription());
        assertEquals(Location.Unspecified, softwareSystem.getLocation());
        assertEquals("Element,Software System", softwareSystem.getTags());
    }

    @Test
    void test_parse_CreatesASoftwareSystemWithADescriptionAndTags() {
        parser.parse(context(), tokens("softwareSystem", "Name", "Description", "Tag 1, Tag 2"));

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("Description", softwareSystem.getDescription());
        assertEquals(Location.Unspecified, softwareSystem.getLocation());
        assertEquals("Element,Software System,Tag 1,Tag 2", softwareSystem.getTags());
    }

    @Test
    void test_parse_CreatesAnInternalSoftwareSystem() {
        EnterpriseDslContext context = new EnterpriseDslContext();
        context.setWorkspace(workspace);
        parser.parse(context, tokens("softwareSystem", "Name"));

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("", softwareSystem.getDescription());
        assertEquals(Location.Internal, softwareSystem.getLocation());
        assertEquals("Element,Software System", softwareSystem.getTags());
    }

}