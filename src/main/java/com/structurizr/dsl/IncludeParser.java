package com.structurizr.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class IncludeParser extends AbstractParser {

    private static final String GRAMMAR = "!include <file|directory|url>";

    private static final int SOURCE_INDEX = 1;

    void parse(IncludedDslContext context, Tokens tokens) {
        // !include <file|directory|url>

        if (tokens.hasMoreThan(SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String source = tokens.get(SOURCE_INDEX);
        if (source.startsWith("https://")) {
            String dsl = readFromUrl(source);
            List<String> lines = Arrays.asList(dsl.split("\n"));
            context.setLines(lines);
            context.setFile(context.getFile());
        } else {
            if (context.getParentFile() != null) {
                File path = new File(context.getParentFile().getParent(), source);

                try {
                    if (!path.exists()) {
                        throw new RuntimeException(path.getCanonicalPath() + " could not be found");
                    }

                    List<String> lines = readLines(path);
                    context.setLines(lines);
                    context.setFile(path);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    private List<String> readLines(File path) throws IOException {
        if (path.isDirectory()) {
            List<String> lines = new ArrayList<>();

            File[] files = path.listFiles();
            if (files != null) {
                Arrays.sort(files);

                for (File file : files) {
                    lines.addAll(readLines(file));
                }
            }

            return lines;
        } else {
            return Files.readAllLines(path.toPath(), StandardCharsets.UTF_8);
        }
    }

}