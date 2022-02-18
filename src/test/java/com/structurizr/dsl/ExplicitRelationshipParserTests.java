package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExplicitRelationshipParserTests extends AbstractTests {

    private ExplicitRelationshipParser parser = new ExplicitRelationshipParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("source", "->", "destination", "description", "technology", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <identifier> -> <identifier> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationIdentifierIsMissing() {
        try {
            parser.parse(context(), tokens("source", "->"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier> -> <identifier> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSourceElementIsNotDefined() {
        try {
            parser.parse(context(), tokens("source", "->", "destination"));
            fail();
        } catch (Exception e) {
            assertEquals("The source element \"source\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationElementIsNotDefined() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", model.addPerson("User", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("source", "->", "destination"));
            fail();
        } catch (Exception e) {
            assertEquals("The destination element \"destination\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_AddsTheRelationship() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescription() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescriptionAndTechnology() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescriptionAndTechnologyAndTags() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP", "Tag 1,Tag 2"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("Relationship,Tag 1,Tag 2", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipAndImplicitRelationshipsWithADescriptionAndTechnologyAndTags() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", container);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP", "Tag 1,Tag 2"));
        assertEquals(2, model.getRelationships().size());

        // this is the relationship that was created
        Relationship r = user.getEfferentRelationshipWith(container);
        assertSame(user, r.getSource());
        assertSame(container, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("Relationship,Tag 1,Tag 2", r.getTags());

        // and this is an implied relationship
        r = user.getEfferentRelationshipWith(softwareSystem);
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationship_WithASourceOfThis() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        GroupableElementDslContext context = new PersonDslContext(user);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("this", "->", "destination"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationship_WithADestinationOfThis() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        GroupableElementDslContext context = new SoftwareSystemDslContext(softwareSystem);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "this"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

}