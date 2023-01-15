package com.structurizr.dsl;

import com.structurizr.view.Border;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.Shape;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ElementStyleParserTests extends AbstractTests {

    private ElementStyleParser parser = new ElementStyleParser();
    private ElementStyle elementStyle;

    private ElementStyleDslContext elementStyleDslContext() {
        elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag");
        ElementStyleDslContext context = new ElementStyleDslContext(elementStyle, new File("."));
        context.setWorkspace(workspace);

        return context;
    }

    @Test
    void test_parseElementStyle_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseElementStyle(context(), tokens("element", "tag", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: element <tag> {", e.getMessage());
        }
    }

    @Test
    void test_parseElementStyle_ThrowsAnException_WhenTheTagIsMissing() {
        try {
            parser.parseElementStyle(context(), tokens("element"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: element <tag> {", e.getMessage());
        }
    }

    @Test
    void test_parseElementStyle_ThrowsAnException_WhenTheTagIsEmpty() {
        try {
            parser.parseElementStyle(context(), tokens("element", ""));
            fail();
        } catch (Exception e) {
            assertEquals("A tag must be specified", e.getMessage());
        }
    }

    @Test
    void test_parseElementStyle_CreatesAnElementStyle() {
        parser.parseElementStyle(context(), tokens("element", "Element"));

        ElementStyle style = workspace.getViews().getConfiguration().getStyles().getElements().stream().filter(es -> "Element".equals(es.getTag())).findFirst().get();
        assertNotNull(style);
    }

    @Test
    void test_parseShape_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseShape(elementStyleDslContext(), tokens("shape", "shape", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: shape <Box|RoundedBox|Circle|Ellipse|Hexagon|Diamond|Cylinder|Pipe|Person|Robot|Folder|WebBrowser|MobileDevicePortrait|MobileDeviceLandscape|Component>", e.getMessage());
        }
    }

    @Test
    void test_parseShape_ThrowsAnException_WhenTheShapeIsMissing() {
        try {
            parser.parseShape(elementStyleDslContext(), tokens("shape"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: shape <Box|RoundedBox|Circle|Ellipse|Hexagon|Diamond|Cylinder|Pipe|Person|Robot|Folder|WebBrowser|MobileDevicePortrait|MobileDeviceLandscape|Component>", e.getMessage());
        }
    }

    @Test
    void test_parseShape_ThrowsAnException_WhenTheShapeIsNotValid() {
        try {
            parser.parseShape(elementStyleDslContext(), tokens("shape", "square"));
            fail();
        } catch (Exception e) {
            assertEquals("The shape \"square\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseShape_SetsTheShape() {
        parser.parseShape(elementStyleDslContext(), tokens("shape", "roundedbox"));
        assertEquals(Shape.RoundedBox, elementStyle.getShape());
    }

    @Test
    void test_parseBackground_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseBackground(elementStyleDslContext(), tokens("background", "hex", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: background <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseBackground_ThrowsAnException_WhenTheBackgroundIsMissing() {
        try {
            parser.parseBackground(elementStyleDslContext(), tokens("background"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: background <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseBackground_SetsTheBackground() {
        parser.parseBackground(elementStyleDslContext(), tokens("background", "#ff0000"));
        assertEquals("#ff0000", elementStyle.getBackground());
    }

    @Test
    void test_parseStroke_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseStroke(elementStyleDslContext(), tokens("stroke", "hex", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: stroke <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseStroke_ThrowsAnException_WhenTheStrokeIsMissing() {
        try {
            parser.parseStroke(elementStyleDslContext(), tokens("stroke"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: stroke <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseStroke_SetsTheStroke() {
        parser.parseStroke(elementStyleDslContext(), tokens("stroke", "#ff0000"));
        assertEquals("#ff0000", elementStyle.getStroke());
    }

    @Test
    void test_parseStrokeWidth_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseStrokeWidth(elementStyleDslContext(), tokens("strokeWidth", "4", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: strokeWidth <1-10>", e.getMessage());
        }
    }

    @Test
    void test_parseStrokeWidth_ThrowsAnException_WhenTheStrokeWidthIsMissing() {
        try {
            parser.parseStrokeWidth(elementStyleDslContext(), tokens("strokeWidth"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: strokeWidth <1-10>", e.getMessage());
        }
    }

    @Test
    void test_parseStrokeWidth_ThrowsAnException_WhenTheStrokeWidthIsNotANumber() {
        try {
            parser.parseStrokeWidth(elementStyleDslContext(), tokens("strokeWidth", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Stroke width must be an integer between 1 and 10", e.getMessage());
        }
    }

    @Test
    void test_parseStrokeWidth_SetsTheStrokeWidth() {
        parser.parseStrokeWidth(elementStyleDslContext(), tokens("strokeWidth", "4"));
        assertEquals(4, elementStyle.getStrokeWidth());
    }

    @Test
    void test_parseColour_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseColour(elementStyleDslContext(), tokens("colour", "hex", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: colour <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseColour_ThrowsAnException_WhenTheColourIsMissing() {
        try {
            parser.parseColour(elementStyleDslContext(), tokens("colour"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: colour <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseColour_SetsTheColour() {
        parser.parseColour(elementStyleDslContext(), tokens("colour", "#ff0000"));
        assertEquals("#ff0000", elementStyle.getColor());
    }

    @Test
    void test_parseBorder_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseBorder(elementStyleDslContext(), tokens("border", "style", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: border <solid|dashed|dotted>", e.getMessage());
        }
    }

    @Test
    void test_parseBorder_ThrowsAnException_WhenTheBorderIsMissing() {
        try {
            parser.parseBorder(elementStyleDslContext(), tokens("border"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: border <solid|dashed|dotted>", e.getMessage());
        }
    }

    @Test
    void test_parseBorder_ThrowsAnException_WhenTheBorderIsNotValid() {
        try {
            parser.parseBorder(elementStyleDslContext(), tokens("border", "rounded"));
            fail();
        } catch (Exception e) {
            assertEquals("The border \"rounded\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseBorder_SetsTheBorder() {
        parser.parseBorder(elementStyleDslContext(), tokens("border", "dotted"));
        assertEquals(Border.Dotted, elementStyle.getBorder());
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseOpacity(elementStyleDslContext(), tokens("opacity", "percentage", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: opacity <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenTheOpacityIsMissing() {
        try {
            parser.parseOpacity(elementStyleDslContext(), tokens("opacity"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: opacity <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenTheOpacityIsNotValid() {
        try {
            parser.parseOpacity(elementStyleDslContext(), tokens("opacity", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Opacity must be an integer between 0 and 100", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_SetsTheOpacity() {
        parser.parseOpacity(elementStyleDslContext(), tokens("opacity", "75"));
        assertEquals(75, elementStyle.getOpacity());
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseWidth(elementStyleDslContext(), tokens("width", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: width <number>", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenTheWidthIsMissing() {
        try {
            parser.parseWidth(elementStyleDslContext(), tokens("width"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: width <number>", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenTheWidthIsNotValid() {
        try {
            parser.parseWidth(elementStyleDslContext(), tokens("width", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Width must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_SetsTheWidth() {
        parser.parseWidth(elementStyleDslContext(), tokens("width", "75"));
        assertEquals(75, elementStyle.getWidth());
    }

    @Test
    void test_parseHeight_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseHeight(elementStyleDslContext(), tokens("height", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: height <number>", e.getMessage());
        }
    }

    @Test
    void test_parseHeight_ThrowsAnException_WhenTheHeightIsMissing() {
        try {
            parser.parseHeight(elementStyleDslContext(), tokens("height"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: height <number>", e.getMessage());
        }
    }

    @Test
    void test_parseHeight_ThrowsAnException_WhenTheHeightIsNotValid() {
        try {
            parser.parseHeight(elementStyleDslContext(), tokens("height", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Height must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseHeight_SetsTheHeight() {
        parser.parseHeight(elementStyleDslContext(), tokens("height", "75"));
        assertEquals(75, elementStyle.getHeight());
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseFontSize(elementStyleDslContext(), tokens("fontSize", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: fontSize <number>", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenTheFontSizeIsMissing() {
        try {
            parser.parseFontSize(elementStyleDslContext(), tokens("fontSize"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: fontSize <number>", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenTheFontSizeIsNotValid() {
        try {
            parser.parseFontSize(elementStyleDslContext(), tokens("fontSize", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Font size must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_SetsTheFontSize() {
        parser.parseFontSize(elementStyleDslContext(), tokens("fontSize", "75"));
        assertEquals(75, elementStyle.getFontSize());
    }

    @Test
    void test_parseMetadata_ThrowsAnException_WhenTheMetadataIsMissing() {
        try {
            parser.parseMetadata(elementStyleDslContext(), tokens("metadata"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: metadata <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseMetadata_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseMetadata(elementStyleDslContext(), tokens("metadata", "boolean", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: metadata <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseMetadata_ThrowsAnException_WhenTheMetadataIsNotValid() {
        try {
            parser.parseMetadata(elementStyleDslContext(), tokens("metadata", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Metadata must be true or false", e.getMessage());
        }
    }

    @Test
    void test_parseMetadata_SetsTheMetadata() {
        ElementStyleDslContext context = elementStyleDslContext();
        parser.parseMetadata(context, tokens("metadata", "false"));
        assertEquals(false, elementStyle.getMetadata());

        parser.parseMetadata(context, tokens("metadata", "true"));
        assertEquals(true, elementStyle.getMetadata());
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseDescription(elementStyleDslContext(), tokens("description", "boolean", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: description <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenTheDescriptionIsMissing() {
        try {
            parser.parseDescription(elementStyleDslContext(), tokens("description"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: description <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenTheDescriptionIsNotValid() {
        try {
            parser.parseDescription(elementStyleDslContext(), tokens("description", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Description must be true or false", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_SetsTheDescription() {
        ElementStyleDslContext context = elementStyleDslContext();
        parser.parseDescription(context, tokens("description", "false"));
        assertEquals(false, elementStyle.getDescription());

        parser.parseDescription(context, tokens("description", "true"));
        assertEquals(true, elementStyle.getDescription());
    }

    @Test
    void test_parseIcon_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseIcon(elementStyleDslContext(), tokens("icon", "file", "extra"), false);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: icon <file|url>", e.getMessage());
        }
    }

    @Test
    void test_parseIcon_ThrowsAnException_WhenTheIconIsMissing() {
        try {
            parser.parseIcon(elementStyleDslContext(), tokens("icon"), false);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: icon <file|url>", e.getMessage());
        }
    }

    @Test
    void test_parseIcon_ThrowsAnException_WhenTheIconDoesNotExist() {
        try {
            parser.parseIcon(elementStyleDslContext(), tokens("icon", "hello.png"), false);
            fail();
        } catch (Exception e) {
            assertEquals("hello.png does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseIcon_SetsTheIconFromADataUri() {
        parser.parseIcon(elementStyleDslContext(), tokens("icon", "data:image/png;base64,123456789012345678901234567890"), true);
        assertTrue(elementStyle.getIcon().startsWith("data:image/png;base64,123456789012345678901234567890"));
    }

    @Test
    void test_parseIcon_SetsTheIconFromAHttpUrl() {
        parser.parseIcon(elementStyleDslContext(), tokens("icon", "http://structurizr.com/logo.png"), true);
        assertEquals("http://structurizr.com/logo.png", elementStyle.getIcon());
    }

    @Test
    void test_parseIcon_SetsTheIconFromAHttpsUrl() {
        parser.parseIcon(elementStyleDslContext(), tokens("icon", "https://structurizr.com/logo.png"), true);
        assertEquals("https://structurizr.com/logo.png", elementStyle.getIcon());
    }

    @Test
    void test_parseIcon_SetsTheIconFromAFile() {
        parser.parseIcon(elementStyleDslContext(), tokens("icon", "src/test/dsl/logo.png"), false);
        System.out.println(elementStyle.getIcon());
        assertTrue(elementStyle.getIcon().startsWith("data:image/png;base64,"));
    }

}