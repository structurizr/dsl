package com.structurizr.dsl;

import java.util.regex.Pattern;

abstract class AbstractParser {

    private static final Pattern VIEW_KEY_PATTERN = Pattern.compile("[\\w-]+");

    void validateViewKey(String key) {
        if (!VIEW_KEY_PATTERN.matcher(key).matches()) {
            throw new RuntimeException("View keys can only contain the following characters: a-zA-0-9_-");
        }
    }

    String removeNonWordCharacters(String name) {
        return name.replaceAll("\\W", "");
    }

}