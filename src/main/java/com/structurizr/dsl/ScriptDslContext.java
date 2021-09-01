package com.structurizr.dsl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

abstract class ScriptDslContext extends DslContext {

    void run(DslContext context, String extension, List<String> lines) throws Exception {
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