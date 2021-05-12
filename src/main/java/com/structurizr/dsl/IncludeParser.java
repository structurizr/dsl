package com.structurizr.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

final class IncludeParser extends AbstractParser {

    private static final String GRAMMAR = "!include <file|url>";

    private static final int FILE_INDEX = 1;

    void parse(IncludedDslContext context, Tokens tokens) {
        // !include <file|url>

        if (tokens.hasMoreThan(FILE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FILE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String source = tokens.get(FILE_INDEX);
        if (source.startsWith("https://")) {
            String dsl = readFromUrl(source);
            List<String> lines = Arrays.asList(dsl.split("\n"));
            context.setLines(lines);
            context.setFile(context.getFile());
        } else {
            if (context.getParentFile() != null) {
                File file = new File(context.getParentFile().getParent(), source);

                try {
                    if (!file.exists()) {
                        throw new RuntimeException(file.getCanonicalPath() + " could not be found");
                    }

                    if (file.isDirectory()) {
                        throw new RuntimeException(file.getCanonicalPath() + " should be a single file");
                    }

                    List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    context.setLines(lines);
                    context.setFile(file);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

}