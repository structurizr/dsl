package com.structurizr.dsl;

final class DynamicViewRelationshipParser extends AbstractParser {

    private final static int URL_INDEX = 1;

    void parseUrl(DynamicViewRelationshipContext context, Tokens tokens) {
        // url <url>
        if (tokens.hasMoreThan(URL_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: url <url>");
        }

        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: url <url>");
        }

        String url = tokens.get(URL_INDEX);
        context.getRelationshipView().setUrl(url);
    }

}