package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.CustomView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomViewContentParserTests extends AbstractTests {

    private CustomViewContentParser parser = new CustomViewContentParser();

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseInclude(new CustomViewDslContext(null), tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>", iae.getMessage());
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

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", softwareSystem);
        context.setElements(elements);

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

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", deploymentNode);
        context.setElements(elements);

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

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        elements.put("box3", box3);
        context.setElements(elements);

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

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

        view.add(box1);
        view.add(box2);
        view.remove(relationship);
        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        parser.parseInclude(context, tokens("include", "box1", "->", "box2"));
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
            assertEquals("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>", iae.getMessage());
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

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

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

        Map<String, Relationship> relationships = new HashMap<>();
        relationships.put("rel", relationship);
        context.setRelationships(relationships);

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

            parser.parseExclude(context, tokens("exclude", "box1", "->", "box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box1\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheView() {
        try {
            CustomElement box1 = model.addCustomElement("Box 1");

            CustomView view = views.createCustomView("key", "Title", "Description");
            CustomViewDslContext context = new CustomViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("box1", box1);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "box1", "->", "box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box1\" does not exist in the view", re.getMessage());
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

            Map<String, Element> elements = new HashMap<>();
            elements.put("box1", box1);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "box1", "->", "box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box2\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheView() {
        try {
            CustomElement box1 = model.addCustomElement("Box 1");
            CustomElement box2 = model.addCustomElement("Box 2");

            CustomView view = views.createCustomView("key", "Title", "Description");
            view.add(box1);
            CustomViewDslContext context = new CustomViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("box1", box1);
            elements.put("box2", box2);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "box1", "->", "box2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"box2\" does not exist in the view", re.getMessage());
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

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "box1", "->", "box2"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndWildcard() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "box1", "->", "*"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndDestination() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "*", "->", "box2"));

        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndWildcard() {
        CustomElement box1 = model.addCustomElement("Box 1");
        CustomElement box2 = model.addCustomElement("Box 2");
        box1.uses(box2, "");

        CustomView view = views.createCustomView("key", "Title", "Description");
        view.add(box1);
        view.add(box2);
        CustomViewDslContext context = new CustomViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("box1", box1);
        elements.put("box2", box2);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        parser.parseExclude(context, tokens("exclude", "*", "->", "*"));

        assertEquals(0, view.getRelationships().size());
    }
    
}