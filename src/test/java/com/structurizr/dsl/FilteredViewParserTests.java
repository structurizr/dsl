package com.structurizr.dsl;

import com.structurizr.view.FilteredView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilteredViewParserTests extends AbstractTests {

    private FilteredViewParser parser = new FilteredViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("filtered", "baseKey", "key", "mode", "tags", "description", "extra"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Too many tokens, expected: filtered <baseKey> <include|exclude> <tags> [key] [description]", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheBaseKeyIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("filtered"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: filtered <baseKey> <include|exclude> <tags> [key] [description]", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheModeIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("filtered", "baseKey"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: filtered <baseKey> <include|exclude> <tags> [key] [description]", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheTagsAreMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("filtered", "baseKey", "include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: filtered <baseKey> <include|exclude> <tags> [key] [description]", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheModeIsInvalid() {
        DslContext context = context();
        views.createDeploymentView("deployment", "Description");
        try {
            parser.parse(context, tokens("filtered", "baseKey", "mode", "Tag 1, Tag 2", "key"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Filter mode should be include or exclude", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheBaseViewDoesNotExist() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("filtered", "baseKey", "include", "Tag 1, Tag 2", "key"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The view \"baseKey\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheBaseViewIsNotAStaticView() {
        DslContext context = context();
        views.createDeploymentView("baseKey", "Description");
        try {
            parser.parse(context, tokens("filtered", "baseKey", "include", "Tag 1, Tag 2", "key"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The view \"baseKey\" must be a System Landscape, System Context, Container, or Component view", iae.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAFilteredView() {
        DslContext context = context();
        views.createSystemLandscapeView("SystemLandscape", "Description");
        parser.parse(context, tokens("filtered", "SystemLandscape", "include", "Tag 1, Tag 2"));
        List<FilteredView> views = new ArrayList<>(context.getWorkspace().getViews().getFilteredViews());

        assertEquals(1, views.size());
        assertEquals("Filtered-001", views.get(0).getKey());
        assertEquals(2, views.get(0).getTags().size());
        assertTrue(views.get(0).getTags().contains("Tag 1"));
        assertTrue(views.get(0).getTags().contains("Tag 2"));
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesAFilteredViewWithAKey() {
        DslContext context = context();
        views.createSystemLandscapeView("SystemLandscape", "Description");
        parser.parse(context, tokens("filtered", "SystemLandscape", "include", "Tag 1, Tag 2", "key"));
        List<FilteredView> views = new ArrayList<>(context.getWorkspace().getViews().getFilteredViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals(2, views.get(0).getTags().size());
        assertTrue(views.get(0).getTags().contains("Tag 1"));
        assertTrue(views.get(0).getTags().contains("Tag 2"));
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesAFilteredViewWithAKeyAndDescription() {
        DslContext context = context();
        views.createSystemLandscapeView("SystemLandscape", "Description");
        parser.parse(context, tokens("filtered", "SystemLandscape", "include", "Tag 1, Tag 2", "key", "Description"));
        List<FilteredView> views = new ArrayList<>(context.getWorkspace().getViews().getFilteredViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals(2, views.get(0).getTags().size());
        assertTrue(views.get(0).getTags().contains("Tag 1"));
        assertTrue(views.get(0).getTags().contains("Tag 2"));
        assertEquals("Description", views.get(0).getDescription());
    }

}