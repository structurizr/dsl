package com.structurizr.dsl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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

            String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);
            List<String> lines = Files.readAllLines(scriptFile.toPath(), StandardCharsets.UTF_8);

            runScript(context, fileExtension, lines);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running script at " + filename + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    void parse(DslContext context, String language, List<String> lines) {
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
                default:
                    throw new RuntimeException("Unsupported scripting language \"" + language + "\"");
            }

            runScript(context, fileExtension, lines);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running inline script, caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void runScript(DslContext context, String extension, List<String> lines) throws Exception {
        StringBuilder script = new StringBuilder();
        for (String line : lines) {
            script.append(line);
            script.append('\n');
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension(extension);

        if (engine != null) {
            Bindings bindings = engine.createBindings();
            bindings.put("workspace", context.getWorkspace());

            engine.eval(script.toString(), bindings);
        } else {
            throw new RuntimeException("Could not load a scripting engine for extension \"" + extension + "\"");
        }
    }

}