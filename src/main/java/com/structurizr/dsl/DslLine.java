package com.structurizr.dsl;

/**
 * Represents a line of DSL, and its line number from the source file.
 */
class DslLine {

    private final String source;
    private final int lineNumber;

    DslLine(String source, int lineNumber) {
        this.source = source;
        this.lineNumber = lineNumber;
    }

    String getSource() {
        return source;
    }

    int getLineNumber() {
        return lineNumber;
    }

}