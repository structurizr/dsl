package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

class InlineScriptDslContext extends ScriptDslContext {

    private final String language;
    private final List<String> lines = new ArrayList<>();

    InlineScriptDslContext(DslContext parentContext, String language) {
        super(parentContext);

        this.language = language;
    }

    void addLine(String line) {
        lines.add(line);
    }

    @Override
    void end() {
        try {
            String fileExtension;

            switch (language.toLowerCase()) {
                case "javascript":
                    fileExtension = "js";
                    break;
                case "groovy":
                    fileExtension = "groovy";
                    break;
                case "kotlin":
                    fileExtension = "kts";
                    break;
                case "ruby":
                    fileExtension = "rb";
                    break;
                default:
                    throw new RuntimeException("Unsupported scripting language \"" + language + "\"");
            }

            run(this, fileExtension, lines);
        } catch (Exception e) {
            throw new RuntimeException("Error running inline script, caused by " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}