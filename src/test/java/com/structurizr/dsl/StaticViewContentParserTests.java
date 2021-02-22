package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StaticViewContentParserTests extends AbstractTests {

    private StaticViewContentParser parser = new StaticViewContentParser();

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseInclude(new SystemLandscapeViewDslContext(null), tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        try {
            parser.parseInclude(new SystemLandscapeViewDslContext(null), tokens("include", "user"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"user\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddAContainerToASystemLandscapeView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("container", container);
        context.setElements(elements);

        try {
            parser.parseInclude(context, tokens("include", "container"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"container\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddAComponentToASystemLandscapeView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        Component component = container.addComponent("Component", "Description", "Technology");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("component", component);
        context.setElements(elements);

        try {
            parser.parseInclude(context, tokens("include", "component"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"component\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_AddsAllPeopleAndSoftwareSystemsToASystemLandscapeView_WhenTheWildcardIsSpecified() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        softwareSystem1.addContainer("Container 1", "Description", "Technology");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        user.uses(softwareSystem1, "Uses");
        softwareSystem1.uses(softwareSystem2, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "*"));

        assertEquals(3, view.getElements().size());
        assertEquals(2, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_AddsTheSpecifiedElementsToASystemLandscapeView() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        user.uses(softwareSystem1, "Uses");
        softwareSystem1.uses(softwareSystem2, "Uses");
        CustomElement box1 = model.addCustomElement("Box 1");
        box1.uses(softwareSystem1, "");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem1", softwareSystem1);
        elements.put("softwaresystem2", softwareSystem2);
        elements.put("box1", box1);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "user", "softwareSystem1", "box1"));

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(user)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(softwareSystem1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(box1)));
        assertTrue(view.getElements().stream().noneMatch(ev -> ev.getElement().equals(softwareSystem2)));
        assertEquals(2, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_AddsNearestNeighboursToASystemContextView_WhenTheWildcardIsSpecified() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        softwareSystem1.addContainer("Container 1", "Description", "Technology");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        user.uses(softwareSystem1, "Uses");
        softwareSystem1.uses(softwareSystem2, "Uses");

        SystemContextView view = views.createSystemContextView(softwareSystem1, "key", "Description");
        SystemContextViewDslContext context = new SystemContextViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "*"));

        assertEquals(3, view.getElements().size());
        assertEquals(2, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_AddsTheSpecifiedPeopleAndSoftwareSystemsToASystemContextView() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        user.uses(softwareSystem1, "Uses");
        softwareSystem1.uses(softwareSystem2, "Uses");

        SystemContextView view = views.createSystemContextView(softwareSystem1, "key", "Description");
        SystemContextViewDslContext context = new SystemContextViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem1", softwareSystem1);
        elements.put("softwaresystem2", softwareSystem2);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "user"));

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(user)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(softwareSystem1)));
        assertTrue(view.getElements().stream().noneMatch(ev -> ev.getElement().equals(softwareSystem2)));
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddAContainerToASystemContextView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");

        SystemContextView view = views.createSystemContextView(softwareSystem, "key", "Description");
        SystemContextViewDslContext context = new SystemContextViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("container", container);
        context.setElements(elements);

        try {
            parser.parseInclude(context, tokens("include", "container"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"container\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddAComponentToASystemContextView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        Component component = container.addComponent("Component", "Description", "Technology");

        SystemContextView view = views.createSystemContextView(softwareSystem, "key", "Description");
        SystemContextViewDslContext context = new SystemContextViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("component", component);
        context.setElements(elements);

        try {
            parser.parseInclude(context, tokens("include", "component"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"component\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_IncludesTheSpecifiedRelationship_WhenARelationshipExpressionIsSpecified() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Relationship relationship = user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        view.add(user);
        view.add(softwareSystem);
        view.remove(relationship);
        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        parser.parseInclude(context, tokens("include", "user", "->", "softwareSystem"));
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseExclude(context, tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseExclude(context, tokens("exclude", "user"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"user\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheSpecifiedPeopleAndSoftwareSystemsFromAView() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        user.uses(softwareSystem1, "Uses");
        softwareSystem1.uses(softwareSystem2, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem1", softwareSystem1);
        elements.put("softwaresystem2", softwareSystem2);
        context.setElements(elements);

        assertEquals(3, view.getElements().size());
        assertEquals(2, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "user", "softwaresystem1"));

        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().stream().noneMatch(ev -> ev.getElement().equals(user)));
        assertTrue(view.getElements().stream().noneMatch(ev -> ev.getElement().equals(softwareSystem1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(softwareSystem2)));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExplicitIdentifierIsSpecified() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Relationship rel = user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Relationship> relationships = new HashMap<>();
        relationships.put("rel", rel);
        context.setRelationships(relationships);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "rel"));

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(user)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(softwareSystem)));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheModel() {
        try {
            SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
            context.setWorkspace(workspace);

            parser.parseExclude(context, tokens("exclude", "user", "->", "softwareSystem"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"user\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheView() {
        try {
            Person user = model.addPerson("User", "Description");
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
            user.uses(softwareSystem, "Uses");

            SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("user", user);
            elements.put("softwaresystem", softwareSystem);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "user", "->", "destination"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"user\" does not exist in the view", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheModel() {
        try {
            Person user = model.addPerson("User", "Description");

            SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
            view.add(user);
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("user", user);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "user", "->", "softwareSystem"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"softwareSystem\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheView() {
        try {
            Person user = model.addPerson("User", "Description");
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
            user.uses(softwareSystem, "Uses");

            SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
            view.add(user);
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("user", user);
            elements.put("softwaresystem", softwareSystem);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "user", "->", "softwareSystem"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"softwareSystem\" does not exist in the view", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndDestination() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "user", "->", "softwareSystem"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndWildcard() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);


        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "user", "->", "*"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndDestination() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);


        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "*", "->", "softwareSystem"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndWildcard() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);


        Map<String, Element> elements = new HashMap<>();
        elements.put("user", user);
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "*", "->", "*"));
        assertEquals(0, view.getRelationships().size());
    }

}