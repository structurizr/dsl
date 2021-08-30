package com.structurizr.dsl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;

final class ScriptParser extends AbstractParser {

    private static final String GRAMMAR = "!script <filename>";

    private static final int FILENAME_INDEX = 1;

    void parse(DslContext context, File file, Tokens tokens) {
        // !plugin <fqn>

        if (tokens.hasMoreThan(FILENAME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FILENAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String filename = tokens.get(FILENAME_INDEX);

        try {
            File scriptFile = new File(file.getParentFile(), filename);
            if (!scriptFile.exists()) {
                throw new RuntimeException("Script file " + scriptFile.getCanonicalPath() + " does not exist");
            }

            ScriptEngineManager manager = new ScriptEngineManager();
            String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);
            ScriptEngine engine = manager.getEngineByExtension(fileExtension);

            if (engine != null) {
                Bindings bindings = engine.createBindings();
                bindings.put("workspace", context.getWorkspace());


                engine.eval(new FileReader(scriptFile), bindings);
            } else {
                throw new RuntimeException("Could not load a scripting engine to run " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running script at " + filename + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}