package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

class InlineScriptDslContext extends ScriptDslContext {

    private String language;
    private List<String> lines = new ArrayList<>();

    InlineScriptDslContext(String language) {
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
            e.printStackTrace();
            throw new RuntimeException("Error running inline script, caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}