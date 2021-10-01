package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticViewContentParserTests extends AbstractTests {

    private StaticViewContentParser parser = new StaticViewContentParser();

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseInclude(new SystemLandscapeViewDslContext(null), tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: include <*|identifier|expression> [*|identifier|expression...]", iae.getMessage());
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("component", component);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem1", softwareSystem1);
        elements.register("softwaresystem2", softwareSystem2);
        elements.register("box1", box1);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem1", softwareSystem1);
        elements.register("softwaresystem2", softwareSystem2);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("component", component);
        context.setIdentifierRegister(elements);

        try {
            parser.parseInclude(context, tokens("include", "component"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"component\" can not be added to this type of view", iae.getMessage());
        }
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
            assertEquals("Expected: exclude <identifier|expression> [identifier|expression...]", iae.getMessage());
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem1", softwareSystem1);
        elements.register("softwaresystem2", softwareSystem2);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister identifersRegister = new IdentifiersRegister();
        identifersRegister.register("rel", rel);
        context.setIdentifierRegister(identifersRegister);

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

            parser.parseExclude(context, tokens("exclude", "relationship.source==user && relationship.destination==softwareSystem"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"user\" does not exist", re.getMessage());
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

            IdentifiersRegister elements = new IdentifiersRegister();
            elements.register("user", user);
            context.setIdentifierRegister(elements);

            parser.parseExclude(context, tokens("exclude", "relationship.source==user && relationship.destination==softwareSystem"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"softwareSystem\" does not exist", re.getMessage());
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.source==user && relationship.destination==softwareSystem"));
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


        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.source==user"));
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


        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.destination==softwareSystem"));
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


        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("user", user);
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship==*"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_AddsAllElementsWithTheSpecifiedTag() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag==Tag 1"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(user));
    }

    @Test
    void test_parseInclude_AddsAllElementsWithTheSpecifiedTags() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1", "Tag 2");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        softwareSystem.addTags("Tag 1");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag==Tag 1,Tag 2"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(user));
    }

    @Test
    void test_parseInclude_AddsAllElementsWithTheSpecifiedTagIgnoringElementsThatAreNotPermitted() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        model.getElements().forEach(e -> e.addTags("Tag 1"));

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag==Tag 1"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));
        assertNull(view.getElementView(container)); // containers are not permitted on system landscape views
        assertNull(view.getElementView(component)); // components are not permitted on system landscape views
    }

    @Test
    void test_parseInclude_AddsAllElementsWithTheSpecifiedTagsIgnoringElementsThatAreNotPermitted() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        model.getElements().forEach(e -> e.addTags("Tag 1", "Tag 2"));

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag==Tag 1,Tag 2"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));
        assertNull(view.getElementView(container)); // containers are not permitted on system landscape views
        assertNull(view.getElementView(component)); // components are not permitted on system landscape views
    }

    @Test
    void test_parseInclude_AddsAllElementsWithoutTheSpecifiedTag() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag!=Tag 1"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseInclude_AddsAllElementsWithoutTheSpecifiedTags() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1", "Tag 2");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        softwareSystem.addTags("Tag 1");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag!=Tag 1,Tag 2"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseInclude_AddsAllElementsWithoutTheSpecifiedTagIgnoringElementsThatAreNotPermitted() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container 1");
        Component component = container.addComponent("Component");

        model.getElements().forEach(e -> e.addTags("Tag 1"));

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "element.tag!=Tag 2"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));
        assertNull(view.getElementView(container)); // containers are not permitted on system landscape views
        assertNull(view.getElementView(component)); // components are not permitted on system landscape views
    }

    @Test
    void test_parseExclude_RemovesAllElementsWithTheSpecifiedTag() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addDefaultElements();
        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "element.tag==Tag 1"));

        assertEquals(1, view.getElements().size());
        assertNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseExclude_RemovesAllElementsWithTheSpecifiedTags() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1", "Tag 2");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        softwareSystem.addTags("Tag 1");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addDefaultElements();
        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "element.tag==Tag 1,Tag 2"));

        assertEquals(1, view.getElements().size());
        assertNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseExclude_RemovesAllElementsWithoutTheSpecifiedTag() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addDefaultElements();
        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "element.tag!=Tag 1"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseExclude_RemovesAllElementsWithoutTheSpecifiedTags() {
        Person user = model.addPerson("User");
        user.addTags("Tag 1", "Tag 2");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        softwareSystem.addTags("Tag 1");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addDefaultElements();
        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNotNull(view.getElementView(softwareSystem));

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "element.tag!=Tag 1,Tag 2"));

        assertEquals(1, view.getElements().size());
        assertNotNull(view.getElementView(user));
        assertNull(view.getElementView(softwareSystem));
    }

    @Test
    void test_parseInclude_AddsAllRelationshipsWithTheSpecifiedTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        Relationship relationship1 = user.uses(softwareSystem, "1");
        relationship1.addTags("Tag 1");
        Relationship relationship2 = user.uses(softwareSystem, "2");
        relationship2.addTags("Tag 2");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        view.remove(relationship1);
        view.remove(relationship2);
        assertEquals(0, view.getRelationships().size());

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "relationship.tag==Tag 1"));

        assertEquals(1, view.getRelationships().size());
        assertNotNull(view.getRelationshipView(relationship1));
    }

    @Test
    void test_parseInclude_AddsAllRelationshipsWithTheSpecifiedTags() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        Relationship relationship1 = user.uses(softwareSystem, "1");
        relationship1.addTags("Tag 1", "Tag 2");
        Relationship relationship2 = user.uses(softwareSystem, "2");
        relationship2.addTags("Tag 2");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        view.remove(relationship1);
        view.remove(relationship2);
        assertEquals(0, view.getRelationships().size());

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "relationship.tag==Tag 1,Tag 2"));

        assertEquals(1, view.getRelationships().size());
        assertNotNull(view.getRelationshipView(relationship1));
    }

    @Test
    void test_parseExclude_RemovesAllRelationshipsWithTheSpecifiedTag() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        Relationship relationship1 = user.uses(softwareSystem, "1");
        relationship1.addTags("Tag 1");
        Relationship relationship2 = user.uses(softwareSystem, "2");
        relationship2.addTags("Tag 2");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        assertEquals(2, view.getRelationships().size());

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "relationship.tag==Tag 1"));

        assertEquals(1, view.getRelationships().size());
        assertNull(view.getRelationshipView(relationship1));
        assertNotNull(view.getRelationshipView(relationship2));
    }

    @Test
    void test_parseExclude_RemovesAllRelationshipsWithTheSpecifiedTags() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        Relationship relationship1 = user.uses(softwareSystem, "1");
        relationship1.addTags("Tag 1", "Tag 2");
        Relationship relationship2 = user.uses(softwareSystem, "2");
        relationship2.addTags("Tag 2");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        view.addAllElements();
        assertEquals(2, view.getRelationships().size());

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseExclude(context, tokens("exclude", "relationship.tag==Tag 1,Tag 2"));

        assertEquals(1, view.getRelationships().size());
        assertNull(view.getRelationshipView(relationship1));
        assertNotNull(view.getRelationshipView(relationship2));
    }

    @Test
    void test_parseInclude_IncludesTheMostAbstractElementWhenEfferentCouplingExpressionUsed() {
        SoftwareSystem ss1 = model.addSoftwareSystem("Software System 1");
        Container c1 = ss1.addContainer("Container 1");
        Component cc1 = c1.addComponent("Component 1");

        SoftwareSystem ss2 = model.addSoftwareSystem("Software System 2");
        Container c2 = ss2.addContainer("Container 2");
        Component cc2 = c2.addComponent("Component 2");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        cc1.uses(cc2, "Uses");

        ComponentView view = views.createComponentView(c1, "key", "Description");
        ComponentViewDslContext context = new ComponentViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("cc1", cc1);
        context.setIdentifierRegister(elements);

        parser.parseInclude(context, tokens("include", "cc1->"));

        assertEquals(2, view.getElements().size());
        assertTrue(view.isElementInView(cc1));
        assertTrue(view.isElementInView(ss2)); // this is the software system, not the component
    }

}