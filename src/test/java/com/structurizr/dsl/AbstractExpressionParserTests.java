package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbstractExpressionParserTests extends AbstractTests {

    private StaticViewExpressionParser parser = new StaticViewExpressionParser();

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipSourceIsSpecifiedUsingLongSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship.source==a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipSourceIsSpecifiedUsingShortSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship==a->*", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipDestinationIsSpecifiedUsingLongSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship.destination==a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipDestinationIsSpecifiedUsingShortSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship==*->a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.source==a", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->*", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipDestinationIsSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.destination==b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipDestinationIsSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==*->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceAndDestinationAreSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.source==a && relationship.destination==b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceAndDestinationAreSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("a", a);
        elements.put("b", b);
        elements.put("c", c);
        context.setElements(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentCouplingExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        context.setElements(map);

        Set<ModelItem> elements = parser.parseExpression("->b", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));

        elements = parser.parseExpression("element==->b", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnEfferentCouplingExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        context.setElements(map);

        Set<ModelItem> elements = parser.parseExpression("b->", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==b->", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentAndEfferentCouplingExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        Map<String, Element> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        context.setElements(map);

        Set<ModelItem> elements = parser.parseExpression("->b->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==->b->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

}