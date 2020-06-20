package com.structurizr.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

final class IncludeParser extends AbstractParser {

    private static final int FILE_INDEX = 1;

    void parse(IncludedDslContext context, Tokens tokens) {
        // include <file>

        if (!tokens.includes(FILE_INDEX)) {
            throw new RuntimeException("Expected: include <file>");
        }

        String filename = tokens.get(FILE_INDEX);
        if (context.getParentFile() != null) {
            File file = new File(context.getParentFile().getParent(), filename);

            try {
                if (!file.exists()) {
                    throw new RuntimeException(file.getCanonicalPath() + " could not be found");
                }

                if (file.isDirectory()) {
                    throw new RuntimeException(file.getCanonicalPath() + " should be a single file");
                }

                context.setLines(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
                context.setFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("The !include feature is unavailable");
        }
    }

}