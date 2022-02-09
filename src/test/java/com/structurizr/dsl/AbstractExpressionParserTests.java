package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.source==a", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingAnExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        a.addTags("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        b.addTags("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        c.addTags("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);
        context.setIdentifierRegister(new IdentifiersRegister());

        Set<ModelItem> relationships = parser.parseExpression("* -> element.tag==B", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("element.tag==A -> *", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("element.tag==A -> element.tag==B", context);
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->*", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("a -> *", context);
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==*->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("* -> b", context);
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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

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

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

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
    void test_parseExpression_ReturnsElements_WhenUsingAnEfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

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
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentAndEfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

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

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentAndEfferentCouplingExpressionWithAnElementExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        b.addTags("Tag 1");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("->element.tag==Tag 1->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==->element.tag==Tag 1->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

    @Test
    void test_parseExpression_ReturnsAllRelationships_WhenUsingTheWildcardRelationshipExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> relationships = parser.parseExpression("relationship==*->*", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("* -> *", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("relationship==*", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));
    }
}