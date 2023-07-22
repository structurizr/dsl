package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.view.ImageView;

class ImageViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "image <*|element identifier> [key] {";

    private static final String VIEW_TYPE = "Image";

    private static final int SCOPE_IDENTIFIER_INDEX = 1;
    private static final int KEY_INDEX = 2;

    private static final String WILDCARD = "*";

    ImageView parse(DslContext context, Tokens tokens) {
        // image <*|element identifier> [key] {

        Workspace workspace = context.getWorkspace();
        String key = "";

        if (tokens.hasMoreThan(KEY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(SCOPE_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        ImageView view;
        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = workspace.getViews().generateViewKey(VIEW_TYPE);
        }
        validateViewKey(key);

        String scopeIdentifier = tokens.get(SCOPE_IDENTIFIER_INDEX);
        if (WILDCARD.equals(scopeIdentifier)) {

            view = workspace.getViews().createImageView(key);
        } else {
            Element element = context.getElement(scopeIdentifier);
            if (element == null) {
                throw new RuntimeException("The element \"" + scopeIdentifier + "\" does not exist");
            }

            view = workspace.getViews().createImageView(element, key);
        }

        return view;
    }

}