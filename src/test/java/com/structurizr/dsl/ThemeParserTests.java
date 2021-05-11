package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ThemeParserTests extends AbstractTests {

    private ThemeParser parser = new ThemeParser();

    @Test
    void test_parseTheme_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseTheme(context(), tokens("theme", "url", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: theme <url>", e.getMessage());
        }
    }

    @Test
    void test_parseTheme_ThrowsAnException_WhenNoThemeIsSpecified() {
        try {
            parser.parseTheme(context(), tokens("theme"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: theme <url>", e.getMessage());
        }
    }

    @Test
    void test_parseTheme_AddsTheTheme_WhenAThemeIsSpecified() {
        parser.parseTheme(context(), tokens("theme", "http://example.com/1"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseTheme_AddsTheTheme_WhenTheDefaultThemeIsSpecified() {
        parser.parseTheme(context(), tokens("theme", "default"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("https://static.structurizr.com/themes/default/theme.json", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseThemes_ThrowsAnException_WhenNoThemesAreSpecified() {
        try {
            parser.parseThemes(context(), tokens("themes"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: themes <url> [url] ... [url]", e.getMessage());
        }
    }

    @Test
    void test_parseThemes_AddsTheTheme_WhenOneThemeIsSpecified() {
        parser.parseThemes(context(), tokens("themes", "http://example.com/1"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseThemes_AddsTheThemes_WhenMultipleThemesAreSpecified() {
        parser.parseThemes(context(), tokens("themes", "http://example.com/1", "http://example.com/2", "http://example.com/3"));

        assertEquals(3, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
        assertEquals("http://example.com/2", workspace.getViews().getConfiguration().getThemes()[1]);
        assertEquals("http://example.com/3", workspace.getViews().getConfiguration().getThemes()[2]);
    }

    @Test
    void test_parseThemes_AddsTheTheme_WhenTheDefaultThemeIsSpecified() {
        parser.parseThemes(context(), tokens("themes", "default"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("https://static.structurizr.com/themes/default/theme.json", workspace.getViews().getConfiguration().getThemes()[0]);
    }

}