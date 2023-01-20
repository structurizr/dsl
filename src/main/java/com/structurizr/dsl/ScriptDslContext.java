package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

abstract class ScriptDslContext extends DslContext {

    private static final String WORKSPACE_VARIABLE_NAME = "workspace";
    private static final String VIEW_VARIABLE_NAME = "view";
    private static final String ELEMENT_VARIABLE_NAME = "element";
    private static final String RELATIONSHIP_VARIABLE_NAME = "relationship";

    private final DslContext parentContext;

    ScriptDslContext(DslContext parentContext) {
        this.parentContext = parentContext;
    }

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
            bindings.put(WORKSPACE_VARIABLE_NAME, context.getWorkspace());

            if (parentContext instanceof ViewDslContext) {
                bindings.put(VIEW_VARIABLE_NAME, ((ViewDslContext)parentContext).getView());
            } else if (parentContext instanceof ModelItemDslContext) {
                ModelItemDslContext modelItemDslContext = (ModelItemDslContext)parentContext;
                if (modelItemDslContext.getModelItem() instanceof Element) {
                    bindings.put(ELEMENT_VARIABLE_NAME, modelItemDslContext.getModelItem());
                } else if (modelItemDslContext.getModelItem() instanceof Relationship) {
                    bindings.put(RELATIONSHIP_VARIABLE_NAME, modelItemDslContext.getModelItem());
                }
            }

            engine.eval(script.toString(), bindings);
        } else {
            throw new RuntimeException("Could not load a scripting engine for extension \"" + extension + "\"");
        }
    }

}