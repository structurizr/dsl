package com.structurizr.dsl;

final class PluginParser extends AbstractParser {

    private static final String GRAMMAR = "!plugin <fqn>";

    private static final int FQN_INDEX = 1;

    void parse(DslContext context, Tokens tokens) {
        // !plugin <fqn>

        if (tokens.hasMoreThan(FQN_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FQN_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String fqn = tokens.get(FQN_INDEX);
        try {
            StructurizrDslPlugin plugin = (StructurizrDslPlugin)Class.forName(fqn).getDeclaredConstructor().newInstance();
            StructurizrDslPluginContext pluginContext = new StructurizrDslPluginContext(context.getWorkspace());

            plugin.run(pluginContext);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running plugin " + fqn + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}