package com.structurizr.dsl;

import com.structurizr.view.CustomView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CustomViewParserTests extends AbstractTests {

    private CustomViewParser parser = new CustomViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("custom", "key", "title", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: custom [key] [title] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesACustomView() {
        DslContext context = context();
        parser.parse(context, tokens("custom"));
        List<CustomView> views = new ArrayList<>(context.getWorkspace().getViews().getCustomViews());

        assertEquals(1, views.size());
        assertEquals("Custom-001", views.get(0).getKey());
        assertEquals("", views.get(0).getTitle());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesACustomViewWithAKey() {
        DslContext context = context();
        parser.parse(context, tokens("custom", "key"));
        List<CustomView> views = new ArrayList<>(context.getWorkspace().getViews().getCustomViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getTitle());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesACustomViewWithAKeyAndTitle() {
        DslContext context = context();
        parser.parse(context, tokens("custom", "key", "Title"));
        List<CustomView> views = new ArrayList<>(context.getWorkspace().getViews().getCustomViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Title", views.get(0).getTitle());
        assertEquals("", views.get(0).getDescription());
    }

    @Test
    void test_parse_CreatesACustomViewWithAKeyAndTitleAndDescription() {
        DslContext context = context();
        parser.parse(context, tokens("custom", "key", "Title", "Description"));
        List<CustomView> views = new ArrayList<>(context.getWorkspace().getViews().getCustomViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Title", views.get(0).getTitle());
        assertEquals("Description", views.get(0).getDescription());
    }

}