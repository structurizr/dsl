package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.StringUtils;
import com.structurizr.view.FilterMode;
import com.structurizr.view.FilteredView;
import com.structurizr.view.StaticView;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

final class FilteredViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "filtered <baseKey> <include|exclude> <tags> [key] [description]";

    private static final String VIEW_TYPE = "Filtered";

    private static final int BASE_KEY_INDEX = 1;
    private static final int MODE_INDEX = 2;
    private static final int TAGS_INDEX = 3;
    private static final int KEY_INDEX = 4;
    private static final int DESCRIPTION_INDEX = 5;

    private static final String FILTER_MODE_INCLUDE = "include";
    private static final String FILTER_MODE_EXCLUDE = "exclude";

    FilteredView parse(DslContext context, Tokens tokens) {
        // filtered <baseKey> <include|exclude> <tags> [key} [description]

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(TAGS_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Workspace workspace = context.getWorkspace();
        String key;

        StaticView baseView;
        String baseKey = tokens.get(BASE_KEY_INDEX);
        String mode = tokens.get(MODE_INDEX);
        String tagsAsString = tokens.get(TAGS_INDEX);
        Set<String> tags = new HashSet<>();

        for (String tag : tagsAsString.split(",")) {
            if (!StringUtils.isNullOrEmpty(tag)) {
                tags.add(tag.trim());
            }
        }

        String description = "";

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        FilterMode filterMode;
        if (FILTER_MODE_INCLUDE.equalsIgnoreCase(mode)) {
            filterMode = FilterMode.Include;
        } else if (FILTER_MODE_EXCLUDE.equalsIgnoreCase(mode)) {
            filterMode = FilterMode.Exclude;
        } else {
            throw new RuntimeException("Filter mode should be include or exclude");
        }

        if (workspace.getViews().getViews().stream().noneMatch(v -> v.getKey().equals(baseKey))) {
            throw new RuntimeException("The view \"" + baseKey + "\" does not exist");
        }

        baseView = (StaticView)workspace.getViews().getViews().stream().filter(v -> v instanceof StaticView && v.getKey().equals(baseKey)).findFirst().orElse(null);
        if (baseView == null) {
            throw new RuntimeException("The view \"" + baseKey + "\" must be a System Landscape, System Context, Container, or Component view");
        }

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = workspace.getViews().generateViewKey(VIEW_TYPE);
        }
        validateViewKey(key);

        return workspace.getViews().createFilteredView(baseView, key, description, filterMode, tags.toArray(new String[0]));
    }

}