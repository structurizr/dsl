package com.structurizr.dsl;

import java.io.File;

/**
 * Throw when there are parsing errors.
 */
public final class StructurizrDslParserException extends Exception {

    /** line number */
    private int lineNumber;

    /** line */
    private String line;

    /**
     * Creates a new instance with the specified message.
     *
     * @param message       the message
     */
    StructurizrDslParserException(String message) {
        super(message);
    }

    StructurizrDslParserException(String message, File dslFile, int lineNumber, String line) {
        super((message.endsWith(".") ? message.substring(0, message.length()-1) : message) + " at line " + lineNumber + (dslFile != null ? " of " + dslFile.getAbsolutePath() : "") + ": " + line.trim());
        this.lineNumber = lineNumber;
        this.line = line;
    }

    /**
     * Gets the line number associated with the parsing exception.
     *
     * @return  the line number, an integer
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Gets the line associated with the parsing exception.
     *
     * @return  the line, as a String
     */
    public String getLine() {
        return line;
    }

}