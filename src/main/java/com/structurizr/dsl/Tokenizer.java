package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

class Tokenizer {

    List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        line = line.trim();

        boolean tokenStarted = false;
        boolean quoted = false;
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (!tokenStarted) {
                if (c == '"') {
                    quoted = true;
                    tokenStarted = true;
                    token = new StringBuilder();
                } else if (Character.isWhitespace(c)) {
                    // skip
                } else {
                    quoted = false;
                    tokenStarted = true;
                    token = new StringBuilder();
                    token.append(c);
                }
            } else {
                if (c == '"' && line.charAt(i-1) == '\\') {
                    // escaped quote
                    token.append(c);
                } else if (quoted && c == '"') {
                    // this is the end of the token
                    tokens.add(token.toString());
                    tokenStarted = false;
                    quoted = false;
                } else if (!quoted && Character.isWhitespace(c)) {
                    tokens.add(token.toString());
                    tokenStarted = false;
                    quoted = false;
                } else {
                    token.append(c);
                }
            }
        }

        if (tokenStarted) {
            tokens.add(token.toString());
        }

        return tokens;
    }

}