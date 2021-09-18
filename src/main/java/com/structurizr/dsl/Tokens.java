package com.structurizr.dsl;

import java.util.List;

final class Tokens {

    private List<String> tokens;

    Tokens(List<String> tokens) {
        this.tokens = tokens;
    }

    String get(int index) {
        return tokens.get(index).trim().replaceAll("\\\\\"", "\"").trim().replaceAll("\\\\n", "\n");
    }

    int size() {
        return tokens.size();
    }

    boolean contains(String token) {
        return tokens.contains(token.trim());
    }

    Tokens withoutContextStartToken() {
        if (tokens.get(tokens.size()-1).equals(DslContext.CONTEXT_START_TOKEN)) {
            return new Tokens(tokens.subList(0, tokens.size()-1));
        } else {
            return this;
        }
    }

    boolean includes(int index) {
        return tokens.size() - 1 >= index;
    }

    boolean hasMoreThan(int index) {
        return includes(index + 1);
    }
    
}