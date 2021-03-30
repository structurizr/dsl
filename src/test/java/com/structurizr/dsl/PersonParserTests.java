package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonParserTests extends AbstractTests {

    private PersonParser parser = new PersonParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("person", "name", "description", "tags", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: person <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("person"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: person <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAPerson() {
        parser.parse(context(), tokens("person", "User"));

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("", user.getDescription());
        assertEquals(Location.Unspecified, user.getLocation());
        assertEquals("Element,Person", user.getTags());
    }

    @Test
    void test_parse_CreatesAPersonWithADescription() {
        parser.parse(context(), tokens("person", "User", "Description"));

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("Description", user.getDescription());
        assertEquals(Location.Unspecified, user.getLocation());
        assertEquals("Element,Person", user.getTags());
    }

    @Test
    void test_parse_CreatesAPersonWithADescriptionAndTags() {
        parser.parse(context(), tokens("person", "User", "Description", "Tag 1, Tag 2"));

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("Description", user.getDescription());
        assertEquals(Location.Unspecified, user.getLocation());
        assertEquals("Element,Person,Tag 1,Tag 2", user.getTags());
    }

    @Test
    void test_parse_CreatesAnInternalPerson() {
        EnterpriseDslContext context = new EnterpriseDslContext();
        context.setWorkspace(workspace);
        parser.parse(context, tokens("person", "User"));

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("", user.getDescription());
        assertEquals(Location.Internal, user.getLocation());
        assertEquals("Element,Person", user.getTags());
    }

}