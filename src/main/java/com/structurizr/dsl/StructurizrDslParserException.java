package com.structurizr.dsl;

public final class StructurizrDslParserException extends Exception {

    private int lineNumber;
    private String line;

    StructurizrDslParserException(String message) {
        super(message);
    }

    StructurizrDslParserException(String message, int lineNumber, String line) {
        super((message.endsWith(".") ? message.substring(0, message.length()-1) : message) + " at line " + lineNumber + ": " + line.trim());
        this.lineNumber = lineNumber;
        this.line = line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() {
        return line;
    }

}