package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ThemesParserTests extends AbstractTests {

    private ThemesParser parser = new ThemesParser();

    @Test
    void test_parse_ThrowsAnException_WhenNoThemesAreSpecified() {
        try {
            parser.parse(context(), tokens("themes"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: themes <themeUrl> [themeUrl] ... [themeUrl]", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheTheme_WhenOneThemeIsSpecified() {
        parser.parse(context(), tokens("themes", "http://example.com/1"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parse_SetsTheTheme_WhenMultipleThemesAreSpecified() {
        parser.parse(context(), tokens("themes", "http://example.com/1", "http://example.com/2", "http://example.com/3"));

        assertEquals(3, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
        assertEquals("http://example.com/2", workspace.getViews().getConfiguration().getThemes()[1]);
        assertEquals("http://example.com/3", workspace.getViews().getConfiguration().getThemes()[2]);
    }

}