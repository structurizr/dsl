package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BrandingParserTests extends AbstractTests {

    private BrandingParser parser = new BrandingParser();

    @Test
    void test_parseLogo_ThrowsAnException_WhenThereAreTooManyTokens() {
        BrandingDslContext context = new BrandingDslContext(null);

        try {
            parser.parseLogo(context, tokens("logo", "path", "extra"), false);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: logo <path|url>", e.getMessage());
        }
    }

    @Test
    void test_parseLogo_ThrowsAnException_WhenNoPathIsSpecified() {
        BrandingDslContext context = new BrandingDslContext(null);

        try {
            parser.parseLogo(context, tokens("logo"), false);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: logo <path|url>", e.getMessage());
        }
    }

    @Test
    void test_parseLogo_ThrowsAnException_WhenTheLogoDoesNotExist() {
        BrandingDslContext context = new BrandingDslContext(new File("."));

        try {
            parser.parseLogo(context, tokens("logo", "hello.png"), false);
            fail();
        } catch (Exception e) {
            assertEquals("hello.png does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseLogo_ThrowsAnException_WhenTheFileIsNotSupported() {
        BrandingDslContext context = new BrandingDslContext(new File("."));

        try {
            parser.parseLogo(context, tokens("logo", "examples/getting-started.dsl"), false);
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage().endsWith("is not a supported image file."));
        }
    }

    @Test
    void test_parseLogo_SetsTheLogo_WhenTheLogoDoesExist() {
        BrandingDslContext context = new BrandingDslContext(new File("."));
        context.setWorkspace(workspace);

        parser.parseLogo(context, tokens("logo", "examples/logo.png"), false);
        assertTrue(workspace.getViews().getConfiguration().getBranding().getLogo().startsWith("data:image/png;base64,"));
    }

    @Test
    void test_parseLogo_SetsTheLogoFromADataUri() {
        BrandingDslContext context = new BrandingDslContext(new File("."));
        context.setWorkspace(workspace);

        parser.parseLogo(context, tokens("logo", "data:image/png;base64,123456789012345678901234567890"), true);
        assertTrue(workspace.getViews().getConfiguration().getBranding().getLogo().startsWith("data:image/png;base64,123456789012345678901234567890"));
    }

    @Test
    void test_parseLogo_SetsTheLogoFromAHttpUrl() {
        BrandingDslContext context = new BrandingDslContext(new File("."));
        context.setWorkspace(workspace);

        parser.parseLogo(context, tokens("logo", "http://structurizr.com/logo.png"), true);
        assertEquals("http://structurizr.com/logo.png", workspace.getViews().getConfiguration().getBranding().getLogo());
    }

    @Test
    void test_parseLogo_SetsTheLogoFromAHttpsUrl() {
        BrandingDslContext context = new BrandingDslContext(new File("."));
        context.setWorkspace(workspace);

        parser.parseLogo(context, tokens("logo", "https://structurizr.com/logo.png"), true);
        assertEquals("https://structurizr.com/logo.png", workspace.getViews().getConfiguration().getBranding().getLogo());
    }

    @Test
    void test_parseFont_ThrowsAnException_WhenThereAreTooManyTokens() {
        BrandingDslContext context = new BrandingDslContext(null);

        try {
            parser.parseFont(context, tokens("font", "name", "url", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: font <name> [url]", e.getMessage());
        }
    }

    @Test
    void test_parseFont_ThrowsAnException_WhenNoNameIsSpecified() {
        BrandingDslContext context = new BrandingDslContext(null);

        try {
            parser.parseFont(context, tokens("font"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: font <name> [url]", e.getMessage());
        }
    }

    @Test
    void test_parseFont_SetsTheFontName() {
        BrandingDslContext context = new BrandingDslContext(null);
        context.setWorkspace(workspace);

        parser.parseFont(context, tokens("font", "Times New Roman"));
        assertEquals("Times New Roman", workspace.getViews().getConfiguration().getBranding().getFont().getName());
    }

    @Test
    void test_parseFont_SetsTheFontUrl() {
        BrandingDslContext context = new BrandingDslContext(null);
        context.setWorkspace(workspace);

        parser.parseFont(context, tokens("font", "Open Sans", "https://fonts.googleapis.com/css2?family=Open+Sans"));
        assertEquals("https://fonts.googleapis.com/css2?family=Open+Sans", workspace.getViews().getConfiguration().getBranding().getFont().getUrl());
    }

}