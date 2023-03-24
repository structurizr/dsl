package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

class CookbookTests extends AbstractTests {

    @Test
    void test() throws Exception {
        File cookbookDirectory = new File("docs/cookbook");
        parseDslFiles(cookbookDirectory);
    }

    private void parseDslFiles(File directory) throws Exception {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    parseDslFiles(file);
                } else if (file.getName().startsWith("example-") && file.getName().endsWith(".dsl")) {
                    parseDslFile(file);
                }
            }
        }
    }

    private void parseDslFile(File file) throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(file);
    }

}