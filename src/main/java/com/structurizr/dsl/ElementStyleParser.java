package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;
import com.structurizr.view.Border;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.Shape;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class ElementStyleParser extends AbstractParser {

    private static final int FIRST_PROPERTY_INDEX = 1;

    ElementStyle parseElementStyle(DslContext context, Tokens tokens) {
        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: element <tag> {");
        } else if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String tag = tokens.get(1);

            if (StringUtils.isNullOrEmpty(tag)) {
                throw new RuntimeException("A tag must be specified");
            }

            Workspace workspace = context.getWorkspace();
            ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().getElementStyle(tag);
            if (elementStyle == null) {
                elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle(tag);
            }

            return elementStyle;
        } else {
            throw new RuntimeException("Expected: element <tag> {");
        }
    }

    void parseShape(ElementStyleDslContext context, Tokens tokens) {
        Map<String, Shape> shapes = new HashMap<>();
        String shapesAsString = "";
        for (Shape shape : Shape.values()) {
            shapes.put(shape.toString().toLowerCase(), shape);
            shapesAsString += shape;
            shapesAsString += "|";
        }
        shapesAsString = shapesAsString.substring(0, shapesAsString.length()-1);

        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: shape <" + shapesAsString + ">");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String shape = tokens.get(1).toLowerCase();

            if (shapes.containsKey(shape)) {
                style.setShape(shapes.get(shape));
            } else {
                throw new RuntimeException("The shape \"" + shape + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: shape <" + shapesAsString + ">");
        }
    }

    void parseBackground(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: background <#rrggbb|color name>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String colour = tokens.get(1);
            style.setBackground(colour);
        } else {
            throw new RuntimeException("Expected: background <#rrggbb|color name>");
        }
    }

    void parseStroke(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: stroke <#rrggbb|color name>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String colour = tokens.get(1);
            style.setStroke(colour);
        } else {
            throw new RuntimeException("Expected: stroke <#rrggbb|color name>");
        }
    }

    void parseStrokeWidth(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: strokeWidth <1-10>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String strokeWidthAsString = tokens.get(1);

            try {
                int strokeWidth = Integer.parseInt(strokeWidthAsString);
                style.setStrokeWidth(strokeWidth);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Stroke width must be an integer between 1 and 10");
            }
        } else {
            throw new RuntimeException("Expected: strokeWidth <1-10>");
        }
    }

    void parseColour(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: colour <#rrggbb|color name>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String colour = tokens.get(1);
            style.setColor(colour);
        } else {
            throw new RuntimeException("Expected: colour <#rrggbb|color name>");
        }
    }

    void parseBorder(ElementStyleDslContext context, Tokens tokens) {
        Map<String, Border> borders = new HashMap<>();
        for (Border border : Border.values()) {
            borders.put(border.toString().toLowerCase(), border);
        }

        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: border <solid|dashed|dotted>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String border = tokens.get(1).toLowerCase();

            if (borders.containsKey(border)) {
                style.setBorder(borders.get(border));
            } else {
                throw new RuntimeException("The border \"" + border + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: border <solid|dashed|dotted>");
        }
    }

    void parseOpacity(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: opacity <0-100>");
        }

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

    void parseWidth(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: width <number>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String widthAsString = tokens.get(1);

            try {
                int width = Integer.parseInt(widthAsString);
                style.setWidth(width);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Width must be a positive integer");
            }
        } else {
            throw new RuntimeException("Expected: width <number>");
        }
    }

    void parseHeight(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: height <number>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String heightAsString = tokens.get(1);

            try {
                int height = Integer.parseInt(heightAsString);
                style.setHeight(height);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Height must be a positive integer");
            }
        } else {
            throw new RuntimeException("Expected: height <number>");
        }
    }

    void parseFontSize(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: fontSize <number>");
        }

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

    void parseMetadata(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: metadata <true|false>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String metadata = tokens.get(1);

            if ("true".equalsIgnoreCase(metadata)) {
                style.setMetadata(true);
            } else if ("false".equalsIgnoreCase(metadata)) {
                style.setMetadata(false);
            } else {
                throw new RuntimeException("Metadata must be true or false");
            }
        } else {
            throw new RuntimeException("Expected: metadata <true|false>");
        }
    }

    void parseDescription(ElementStyleDslContext context, Tokens tokens) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <true|false>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String description = tokens.get(1);

            if ("true".equalsIgnoreCase(description)) {
                style.setDescription(true);
            } else if ("false".equalsIgnoreCase(description)) {
                style.setDescription(false);
            } else {
                throw new RuntimeException("Description must be true or false");
            }
        } else {
            throw new RuntimeException("Expected: description <true|false>");
        }
    }

    void parseIcon(ElementStyleDslContext context, Tokens tokens, boolean restricted) {
        ElementStyle style = context.getStyle();

        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: icon <file|url>");
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String path = tokens.get(1);

            if (path.startsWith("data:image/") || path.startsWith("https://") || path.startsWith("http://")) {
                if (IconUtils.isSupported(path)) {
                    style.setIcon(path);
                } else {
                    throw new IllegalArgumentException("Only PNG and JPG URLs/data URIs are supported: " + path);
                }
            } else {
                if (!restricted) {
                    File file = new File(context.getFile().getParent(), path);
                    if (file.exists() && !file.isDirectory()) {
                        try {
                            style.setIcon(ImageUtils.getImageAsDataUri(file));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new RuntimeException(path + " does not exist");
                    }
                }
            }
        } else {
            throw new RuntimeException("Expected: icon <file|url>");
        }
    }

}