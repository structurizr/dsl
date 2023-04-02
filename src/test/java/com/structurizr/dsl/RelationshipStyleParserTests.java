package com.structurizr.dsl;

import com.structurizr.view.Border;
import com.structurizr.view.LineStyle;
import com.structurizr.view.RelationshipStyle;
import com.structurizr.view.Routing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationshipStyleParserTests extends AbstractTests {

    private RelationshipStyleParser parser = new RelationshipStyleParser();
    private RelationshipStyle relationshipStyle;

    private RelationshipStyleDslContext relationshipStyleDslContext() {
        relationshipStyle = workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Tag");
        RelationshipStyleDslContext context = new RelationshipStyleDslContext(relationshipStyle);
        context.setWorkspace(workspace);

        return context;
    }

    @Test
    void test_parseRelationshipStyle_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseRelationshipStyle(context(), tokens("relationship", "tag", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: relationship <tag> {", e.getMessage());
        }
    }

    @Test
    void test_parseRelationshipStyle_ThrowsAnException_WhenTheTagIsMissing() {
        try {
            parser.parseRelationshipStyle(context(), tokens("relationship"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: relationship <tag> {", e.getMessage());
        }
    }

    @Test
    void test_parseRelationshipStyle_ThrowsAnException_WhenTheTagIsEmpty() {
        try {
            parser.parseRelationshipStyle(context(), tokens("relationship", ""));
            fail();
        } catch (Exception e) {
            assertEquals("A tag must be specified", e.getMessage());
        }
    }

    @Test
    void test_parseRelationshipStyle_CreatesAnRelationshipStyle() {
        parser.parseRelationshipStyle(context(), tokens("relationship", "Relationship"));

        RelationshipStyle style = workspace.getViews().getConfiguration().getStyles().getRelationships().stream().filter(es -> "Relationship".equals(es.getTag())).findFirst().get();
        assertNotNull(style);
    }

    @Test
    void test_parseRelationshipStyle_FindsAnExistingRelationshipStyle() {
        RelationshipStyle style = workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Tag");
        assertSame(style, parser.parseRelationshipStyle(context(), tokens("relationship", "Tag")));
    }

    @Test
    void test_parseThickness_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseThickness(relationshipStyleDslContext(), tokens("thickness", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: thickness <number>", e.getMessage());
        }
    }

    @Test
    void test_parseThickness_ThrowsAnException_WhenTheThicknessIsMissing() {
        try {
            parser.parseThickness(relationshipStyleDslContext(), tokens("thickness"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: thickness <number>", e.getMessage());
        }
    }

    @Test
    void test_parseThickness_ThrowsAnException_WhenTheThicknessIsNotValid() {
        try {
            parser.parseThickness(relationshipStyleDslContext(), tokens("thickness", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Thickness must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseThickness_SetsTheThickness() {
        parser.parseThickness(relationshipStyleDslContext(), tokens("thickness", "75"));
        assertEquals(75, relationshipStyle.getThickness());
    }

    @Test
    void test_parseColour_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseColour(relationshipStyleDslContext(), tokens("colour", "hex", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: colour <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseColour_ThrowsAnException_WhenTheColourIsMissing() {
        try {
            parser.parseColour(relationshipStyleDslContext(), tokens("colour"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: colour <#rrggbb|color name>", e.getMessage());
        }
    }

    @Test
    void test_parseColour_SetsTheColourWhenUsingAHexColourCode() {
        parser.parseColour(relationshipStyleDslContext(), tokens("colour", "#ff0000"));
        assertEquals("#ff0000", relationshipStyle.getColor());
    }

    @Test
    void test_parseColour_SetsTheColourWhenUsingAColourName() {
        parser.parseColour(relationshipStyleDslContext(), tokens("colour", "yellow"));
        assertEquals("#ffff00", relationshipStyle.getColor());
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseOpacity(relationshipStyleDslContext(), tokens("opacity", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: opacity <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenTheOpacityIsMissing() {
        try {
            parser.parseOpacity(relationshipStyleDslContext(), tokens("opacity"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: opacity <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_ThrowsAnException_WhenTheOpacityIsNotValid() {
        try {
            parser.parseOpacity(relationshipStyleDslContext(), tokens("opacity", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Opacity must be an integer between 0 and 100", e.getMessage());
        }
    }

    @Test
    void test_parseOpacity_SetsTheOpacity() {
        parser.parseOpacity(relationshipStyleDslContext(), tokens("opacity", "75"));
        assertEquals(75, relationshipStyle.getOpacity());
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseWidth(relationshipStyleDslContext(), tokens("width", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: width <number>", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenTheWidthIsMissing() {
        try {
            parser.parseWidth(relationshipStyleDslContext(), tokens("width"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: width <number>", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_ThrowsAnException_WhenTheWidthIsNotValid() {
        try {
            parser.parseWidth(relationshipStyleDslContext(), tokens("width", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Width must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseWidth_SetsTheWidth() {
        parser.parseWidth(relationshipStyleDslContext(), tokens("width", "75"));
        assertEquals(75, relationshipStyle.getWidth());
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseFontSize(relationshipStyleDslContext(), tokens("fontSize", "number", "extrta"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: fontSize <number>", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenTheFontSizeIsMissing() {
        try {
            parser.parseFontSize(relationshipStyleDslContext(), tokens("fontSize"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: fontSize <number>", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_ThrowsAnException_WhenTheFontSizeIsNotValid() {
        try {
            parser.parseFontSize(relationshipStyleDslContext(), tokens("fontSize", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Font size must be a positive integer", e.getMessage());
        }
    }

    @Test
    void test_parseFontSize_SetsTheFontSize() {
        parser.parseFontSize(relationshipStyleDslContext(), tokens("fontSize", "75"));
        assertEquals(75, relationshipStyle.getFontSize());
    }

    @Test
    void test_parseDashed_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseDashed(relationshipStyleDslContext(), tokens("dashed", "boolean", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: dashed <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseDashed_ThrowsAnException_WhenTheDashedIsMissing() {
        try {
            parser.parseDashed(relationshipStyleDslContext(), tokens("dashed"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: dashed <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parseDashed_ThrowsAnException_WhenTheDashedIsNotValid() {
        try {
            parser.parseDashed(relationshipStyleDslContext(), tokens("dashed", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Dashed must be true or false", e.getMessage());
        }
    }

    @Test
    void test_parseDashed_SetsTheDashed() {
        RelationshipStyleDslContext context = relationshipStyleDslContext();
        parser.parseDashed(context, tokens("dashed", "false"));
        assertEquals(false, relationshipStyle.getDashed());

        parser.parseDashed(context, tokens("dashed", "true"));
        assertEquals(true, relationshipStyle.getDashed());
    }

    @Test
    void test_parseLineStyle_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseLineStyle(relationshipStyleDslContext(), tokens("style", "solid", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: style <solid|dashed|dotted>", e.getMessage());
        }
    }

    @Test
    void test_parseLineStyle_ThrowsAnException_WhenTheStyleIsMissing() {
        try {
            parser.parseLineStyle(relationshipStyleDslContext(), tokens("style"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: style <solid|dashed|dotted>", e.getMessage());
        }
    }

    @Test
    void test_parseLineStyle_ThrowsAnException_WhenTheStyleIsNotValid() {
        try {
            parser.parseLineStyle(relationshipStyleDslContext(), tokens("style", "none"));
            fail();
        } catch (Exception e) {
            assertEquals("The line style \"none\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseLineStyle_SetsTheStyle() {
        parser.parseLineStyle(relationshipStyleDslContext(), tokens("style", "dotted"));
        assertEquals(LineStyle.Dotted, relationshipStyle.getStyle());
    }

    @Test
    void test_parsePosition_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parsePosition(relationshipStyleDslContext(), tokens("position", "number", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: position <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parsePosition_ThrowsAnException_WhenThePositionIsMissing() {
        try {
            parser.parsePosition(relationshipStyleDslContext(), tokens("position"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: position <0-100>", e.getMessage());
        }
    }

    @Test
    void test_parsePosition_ThrowsAnException_WhenThePositionIsNotValid() {
        try {
            parser.parsePosition(relationshipStyleDslContext(), tokens("position", "abc"));
            fail();
        } catch (Exception e) {
            assertEquals("Position must be an integer between 0 and 100", e.getMessage());
        }
    }

    @Test
    void test_parsePosition_SetsThePosition() {
        parser.parsePosition(relationshipStyleDslContext(), tokens("position", "75"));
        assertEquals(75, relationshipStyle.getPosition());
    }

    @Test
    void test_parseRouting_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseRouting(relationshipStyleDslContext(), tokens("routing", "enum", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: routing <direct|orthogonal|curved>", e.getMessage());
        }
    }

    @Test
    void test_parseRouting_ThrowsAnException_WhenTheRoutingIsMissing() {
        try {
            parser.parseRouting(relationshipStyleDslContext(), tokens("routing"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: routing <direct|orthogonal|curved>", e.getMessage());
        }
    }

    @Test
    void test_parseRouting_ThrowsAnException_WhenTheRoutingIsNotValid() {
        try {
            parser.parseRouting(relationshipStyleDslContext(), tokens("routing", "rounded"));
            fail();
        } catch (Exception e) {
            assertEquals("The routing \"rounded\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseRouting_SetsTheRouting() {
        parser.parseRouting(relationshipStyleDslContext(), tokens("routing", "curved"));
        assertEquals(Routing.Curved, relationshipStyle.getRouting());
    }

}