package com.structurizr.dsl;

import com.structurizr.configuration.Visibility;
import com.structurizr.configuration.WorkspaceScope;

final class ConfigurationParser extends AbstractParser {

    private static final String SCOPE_GRAMMAR = "scope <landscape|softwaresystem|none>";
    private static final String SCOPE_LANDSCAPE = "landscape";
    private static final String SCOPE_SOFTWARE_SYSTEM = "softwaresystem";
    private static final String SCOPE_NONE = "none";

    private static final String VISIBILITY_GRAMMAR = "visibility <private|public>";
    private static final String VISIBILITY_PRIVATE = "private";
    private static final String VISIBILITY_PUBLIC = "public";

    private static final int FIRST_PROPERTY_INDEX = 1;

    void parseScope(DslContext context, Tokens tokens) {
        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + SCOPE_GRAMMAR);
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String scope = tokens.get(1).toLowerCase();

            if (scope.equalsIgnoreCase(SCOPE_LANDSCAPE)) {
                context.getWorkspace().getConfiguration().setScope(WorkspaceScope.Landscape);
            } else if (scope.equalsIgnoreCase(SCOPE_SOFTWARE_SYSTEM)) {
                context.getWorkspace().getConfiguration().setScope(WorkspaceScope.SoftwareSystem);
            } else if (scope.equalsIgnoreCase(SCOPE_NONE)) {
                context.getWorkspace().getConfiguration().setScope(null);
            } else {
                throw new RuntimeException("The scope \"" + scope + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: " + SCOPE_GRAMMAR);
        }
    }

    void parseVisibility(DslContext context, Tokens tokens) {
        if (tokens.hasMoreThan(FIRST_PROPERTY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + VISIBILITY_GRAMMAR);
        }

        if (tokens.includes(FIRST_PROPERTY_INDEX)) {
            String visibility = tokens.get(1).toLowerCase();

            if (visibility.equalsIgnoreCase(VISIBILITY_PRIVATE)) {
                context.getWorkspace().getConfiguration().setVisibility(Visibility.Private);
            } else if (visibility.equalsIgnoreCase(VISIBILITY_PUBLIC)) {
                context.getWorkspace().getConfiguration().setVisibility(Visibility.Public);
            } else {
                throw new RuntimeException("The visibility \"" + visibility + "\" is not valid");
            }
        } else {
            throw new RuntimeException("Expected: " + VISIBILITY_GRAMMAR);
        }
    }

}