package com.structurizr.dsl;

import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;
import com.structurizr.view.RelationshipView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DynamicViewRelationshipParserTests extends AbstractTests {

    private final DynamicViewRelationshipParser parser = new DynamicViewRelationshipParser();

    @Test
    void test_parseUrl_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            DynamicViewRelationshipContext context = new DynamicViewRelationshipContext(null);
            parser.parseUrl(context, tokens("url", "url", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: url <url>", e.getMessage());
        }
    }

    @Test
    void test_parseUrl_ThrowsAnException_WhenNoUrlIsSpecified() {
        try {
            DynamicViewRelationshipContext context = new DynamicViewRelationshipContext(null);
            parser.parseUrl(context, tokens("url"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: url <url>", e.getMessage());
        }
    }

    @Test
    void test_parseUrl_SetsTheUrl_WhenAUrlIsSpecified() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        Relationship r = a.uses(b, "Uses");

        DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
        RelationshipView rv = dynamicView.add(r);
        DynamicViewRelationshipContext context = new DynamicViewRelationshipContext(rv);
        parser.parseUrl(context, tokens("url", "http://example.com"));

        assertEquals("http://example.com", rv.getUrl());
    }

}