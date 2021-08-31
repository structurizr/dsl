package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

class InlineScriptDslContext extends DslContext {

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
        new ScriptParser().parse(this, language, lines);
    }

}