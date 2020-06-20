package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.view.RelationshipStyle;
import com.structurizr.view.Routing;

import java.util.HashMap;
import java.util.Map;

final class RelationshipStyleParser extends AbstractParser {

    private static final int FIRST_PROPERTY_INDEX = 1;

    RelationshipStyle parseRelationshipStyle(DslContext context, Tokens tokens) {
        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String tag = tokens.get(1);
            Workspace workspace = context.getWorkspace();
            return workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(tag);
        } else {
            throw new RuntimeException("Expected: relationship <tag> {");
        }
    }

    void parseThickness(RelationshipStyleDslContext context, Tokens tokens) {
        // thickness <number>
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String thicknessAsString = tokens.get(1);

            try {
                int thickness = Integer.parseInt(thicknessAsString);
                style.setThickness(thickness);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Thickness must be a positive integer");
            }
        } else {
            throw new RuntimeException("Expected: thickness <number>");
        }
    }

    void parseColour(RelationshipStyleDslContext context, Tokens tokens) {
        // colour #rrggbb
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String colour = tokens.get(1);
            style.setColor(colour);
        } else {
            throw new RuntimeException("Expected: colour <#rrggbb>");
        }
    }

    void parseDashed(RelationshipStyleDslContext context, Tokens tokens) {
        // dashed true|false
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String dashed = tokens.get(1);

            if ("true".equalsIgnoreCase(dashed)) {
                style.setDashed(true);
            } else if ("false".equalsIgnoreCase(dashed)) {
                style.setDashed(false);
            } else {
                throw new RuntimeException("Dashed must be true or false");
            }
        } else {
            throw new RuntimeException("Expected: dashed <true|false>");
        }
    }

    void parseOpacity(RelationshipStyleDslContext context, Tokens tokens) {
        // opacity 0-100
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String opacityAsString = tokens.get(1);

            try {
                int opacity = Integer.parseInt(opacityAsString);
                style.setOpacity(opacity);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Opacity must be an integer between 0 and 100");
            }
        } else {
            throw new RuntimeException("Expected: opacity <0-100>");
        }
    }

    void parseWidth(RelationshipStyleDslContext context, Tokens tokens) {
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String widthAsString = tokens.get(1);

            try {
                int width = Integer.parseInt(widthAsString);
                style.setWidth(width);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Width must be a positive integer");
            }
        } else {
            throw new RuntimeException("Expected: width <number>");
        }
    }

    void parseFontSize(RelationshipStyleDslContext context, Tokens tokens) {
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String fontSizeAsString = tokens.get(1);

            try {
                int fontSize = Integer.parseInt(fontSizeAsString);
                style.setFontSize(fontSize);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Font size must be a positive integer");
            }
        } else {
            throw new RuntimeException("Expected: fontSize <number>");
        }
    }

    void parsePosition(RelationshipStyleDslContext context, Tokens tokens) {
        // position 0-100
        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String positionAsString = tokens.get(1);

            try {
                int opacity = Integer.parseInt(positionAsString);
                style.setPosition(opacity);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Position must be an integer between 0 and 100");
            }
        } else {
            throw new RuntimeException("Expected: position <0-100>");
        }
    }

    void parseRouting(RelationshipStyleDslContext context, Tokens tokens) {
        // routing direct|orthogonal|curved
        Map<String, Routing> routings = new HashMap<>();
        for (Routing routing : Routing.values()) {
            routings.put(routing.toString().toLowerCase(), routing);
        }

        RelationshipStyle style = context.getStyle();

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String routing = tokens.get(1).toLowerCase();

            if (routings.containsKey(routing)) {
                style.setRouting(routings.get(routing));
            } else {
                throw new RuntimeException("The routing \"" + routing + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: routing <direct|orthogonal|curved>");
        }
    }

}