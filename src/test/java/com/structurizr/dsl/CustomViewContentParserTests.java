package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.CustomView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomViewContentParserTests extends AbstractTests {

    private CustomViewContentParser parser = new CustomViewContentParser();

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseInclude(new CustomViewDslContext(null), tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: include <*|identifier> [*|identifier...]", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        try {
            parser.parseInclude(new CustomViewDslContext(null), tokens("include", "box"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"box\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddAStaticStructureElementToACustomView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("element", softwareSystem);
        context.setIdentifierRegister(elements);

        try {
            parser.parseInclude(context, tokens("include", "element"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"element\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTryingToAddADeploymentElementToACustomView() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");

        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("element", deploymentNode);
        context.setIdentifierRegister(elements);

        try {
            parser.parseInclude(context, tokens("include", "element"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element \"element\" can not be added to this type of view", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_AddsAllCustomElementsToA_WhenTheWildcardIsSpecified() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "*"));

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_AddsTheSpecifiedPeopleAndSoftwareSystemsToASystemLandscapeView() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        CustomElement box3 = model.addCustomElement("Box 3");
        box1.uses(box2, "");
        box2.uses(box3, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        elements.register("box3", box3);
        context.setIdentifierRegister(elements);

        parser.parseInclude(context, tokens("include", "box1", "box2"));

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(box1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(box2)));
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_parseInclude_IncludesTheSpecifiedRelationship_WhenARelationshipExpressionIsSpecified() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        Relationship relationship = box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        view.add(box1);
        view.add(box2);
        view.remove(relationship);
        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        parser.parseInclude(context, tokens("include", "relationship.source==box1 && relationship.destination==box2"));
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseExclude(context, tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: exclude <identifier> [identifier...]", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        CustomView view = views.createCustomView("key", "Title", "Description");
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseExclude(context, tokens("exclude", "box"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"box\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheSpecifiedElementsFromAView() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "box2"));

        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().stream().noneMatch(ev -> ev.getElement().equals(box2)));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExplicitIdentifierIsSpecified() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        Relationship relationship = box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister identifersRegister = new IdentifiersRegister();
        identifersRegister.register("rel", relationship);
        context.setIdentifierRegister(identifersRegister);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "rel"));

        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheModel() {
        try {
            CustomView view = views.createCustomView("key", "Title", "Description");
            CustomViewDslContext context = new CustomViewDslContext(view);
            context.setWorkspace(workspace);

            parser.parseExclude(context, tokens("exclude", "relationship.source==box1 && relationship.destination==box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box1\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheModel() {
        try {
            CustomElement box1 = model.addCustomElement("Box 1");

            CustomView view = views.createCustomView("key", "Title", "Description");
            view.add(box1);
            CustomViewDslContext context = new CustomViewDslContext(view);
            context.setWorkspace(workspace);

            IdentifiersRegister elements = new IdentifiersRegister();
            elements.register("box1", box1);
            context.setIdentifierRegister(elements);

            parser.parseExclude(context, tokens("exclude", "relationship.source==box1 && relationship.source==box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box2\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndDestination() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.source==box1 && relationship.destination==box2"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSource() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.source==box1"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithADestination() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship.destination==box2"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesAllRelationshipsFromAView_WhenAnExpressionIsSpecifiedWithAWildcard() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("box1", box1);
        elements.register("box2", box2);
        context.setIdentifierRegister(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "relationship==*"));

        assertEquals(0, view.getRelationships().size());
    }
    
}