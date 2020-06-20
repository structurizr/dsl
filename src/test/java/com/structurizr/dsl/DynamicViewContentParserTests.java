package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;
import com.structurizr.view.RelationshipView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DynamicViewContentParserTests extends AbstractTests {

    private DynamicViewContentParser parser = new DynamicViewContentParser();

    @Test
    void test_parseRelationship_ThrowsAnException_WhenTheDestinationIdentifierIsMissing() {
        try {
            parser.parseRelationship(new DynamicViewDslContext(null), tokens("source", "->"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier> -> <identifier> [description]", e.getMessage());
        }
    }

    @Test
    void test_parseRelationship_ThrowsAnException_WhenTheSourceElementIsNotDefined() {
        DynamicViewDslContext context = new DynamicViewDslContext(null);

        try {
            parser.parseRelationship(context, tokens("source", "->", "destination"));
            fail();
        } catch (Exception e) {
            assertEquals("The source element \"source\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseRelationship_ThrowsAnException_WhenTheDestinationElementIsNotDefined() {
        DynamicViewDslContext context = new DynamicViewDslContext(null);
        Map<String, Element> elements = new HashMap<>();
        elements.put("source", model.addPerson("User", "Description"));
        context.setElements(elements);

        try {
            parser.parseRelationship(context, tokens("source", "->", "destination"));
            fail();
        } catch (Exception e) {
            assertEquals("The destination element \"destination\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseRelationship_AddsTheRelationshipToTheView_WhenItAlreadyExistsInTheModel() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");
        DynamicView view = views.createDynamicView("key", "Description");
        DynamicViewDslContext context = new DynamicViewDslContext(view);

        Map<String, Element> elements = new HashMap<>();
        elements.put("source", user);
        elements.put("destination", softwareSystem);
        context.setElements(elements);

        parser.parseRelationship(context, tokens("source", "->", "destination"));

        assertEquals(1, view.getRelationships().size());
        RelationshipView rv = view.getRelationships().iterator().next();
        assertSame(user, rv.getRelationship().getSource());
        assertSame(softwareSystem, rv.getRelationship().getDestination());
        assertEquals("", rv.getDescription());
        assertEquals("1", rv.getOrder());
    }

    @Test
    void test_parseRelationship_AddsTheRelationshipToTheViewWithAnOverridenDescription_WhenItAlreadyExistsInTheModel() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");
        DynamicView view = views.createDynamicView("key", "Description");
        DynamicViewDslContext context = new DynamicViewDslContext(view);

        Map<String, Element> elements = new HashMap<>();
        elements.put("source", user);
        elements.put("destination", softwareSystem);
        context.setElements(elements);

        parser.parseRelationship(context, tokens("source", "->", "destination", "Does something with"));

        assertEquals(1, view.getRelationships().size());
        RelationshipView rv = view.getRelationships().iterator().next();
        assertSame(user, rv.getRelationship().getSource());
        assertSame(softwareSystem, rv.getRelationship().getDestination());
        assertEquals("Does something with", rv.getDescription());
        assertEquals("1", rv.getOrder());
    }

    @Test
    void test_parseRelationship_AddsTheRelationshipToTheModelAndView_WhenItDoesNotAlreadyExistInTheModel() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DynamicView view = views.createDynamicView("key", "Description");
        DynamicViewDslContext context = new DynamicViewDslContext(view);

        Map<String, Element> elements = new HashMap<>();
        elements.put("source", user);
        elements.put("destination", softwareSystem);
        context.setElements(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parseRelationship(context, tokens("source", "->", "destination", "Uses"));

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());

        assertEquals(1, view.getRelationships().size());
        RelationshipView rv = view.getRelationships().iterator().next();
        assertSame(user, rv.getRelationship().getSource());
        assertSame(softwareSystem, rv.getRelationship().getDestination());
        assertEquals("Uses", rv.getDescription());
        assertEquals("1", rv.getOrder());
    }

}