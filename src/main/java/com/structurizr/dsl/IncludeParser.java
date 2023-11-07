package com.structurizr.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
            context.addFile(context.getParentFile(), lines);
        } else {
            if (context.getParentFile() != null) {
                File path = new File(context.getParentFile().getParent(), source);

                try {
                    if (!path.exists()) {
                        throw new RuntimeException(path.getCanonicalPath() + " could not be found");
                    }

                    readFiles(context, path);
                } catch (IOException e) {
                    throw new RuntimeException("Error including " + path.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
    }

    private void readFiles(IncludedDslContext context, File path) throws IOException {
        if (path.isHidden() || path.getName().startsWith(".")) {
            // ignore
            return;
        }

        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                Arrays.sort(files);

                for (File file : files) {
                    readFiles(context, file);
                }
            }
        } else {
            try {
                context.addFile(path, Files.readAllLines(path.toPath(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException("Error reading file at " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

}