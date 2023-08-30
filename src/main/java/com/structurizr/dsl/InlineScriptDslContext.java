package com.structurizr.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InlineScriptDslContext extends ScriptDslContext {

    static final Map<String,String> SUPPORTED_LANGUAGES = new HashMap<>();

    private final String language;
    private final List<String> lines = new ArrayList<>();

    static {
        SUPPORTED_LANGUAGES.put("javascript", "js");
        SUPPORTED_LANGUAGES.put("groovy", "groovy");
        SUPPORTED_LANGUAGES.put("kotlin", "kts");
        SUPPORTED_LANGUAGES.put("ruby", "rb");
    }

    InlineScriptDslContext(DslContext parentContext, File dslFile, String language) {
        super(parentContext, dslFile);

        this.language = language.toLowerCase();
    }

    void addLine(String line) {
        lines.add(line);
    }

    @Override
    void end() {
        try {
            String fileExtension;

            if (SUPPORTED_LANGUAGES.containsKey(language)) {
                fileExtension = SUPPORTED_LANGUAGES.get(language);
            } else {
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