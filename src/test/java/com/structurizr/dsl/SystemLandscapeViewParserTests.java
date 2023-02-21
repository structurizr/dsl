package com.structurizr.dsl;

import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SystemLandscapeViewParserTests extends AbstractTests {

    private SystemLandscapeViewParser parser = new SystemLandscapeViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("systemLandscape", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: systemLandscape [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASystemLandscapeView() {
        DslContext context = context();
        parser.parse(context, tokens("systemLandscape"));
        List<SystemLandscapeView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemLandscapeViews());

        assertEquals(1, views.size());
        assertEquals("SystemLandscape-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemLandscapeViewWithAKey() {
        DslContext context = context();
        parser.parse(context, tokens("systemLandscape", "key"));
        List<SystemLandscapeView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemLandscapeViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesASystemLandscapeViewWithAKeyAndDescription() {
        DslContext context = context();
        parser.parse(context, tokens("systemLandscape", "key", "description"));
        List<SystemLandscapeView> views = new ArrayList<>(context.getWorkspace().getViews().getSystemLandscapeViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("description", views.get(0).getDescription());
    }

}