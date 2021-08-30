package com.structurizr.dsl;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

final class PluginParser extends AbstractParser {

    private static final String GRAMMAR = "!plugin <fqn>";

    private static final int FQN_INDEX = 1;

    void parse(DslContext context, File file, Tokens tokens) {
        // !plugin <fqn>

        if (tokens.hasMoreThan(FQN_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(FQN_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String fqn = tokens.get(FQN_INDEX);
        try {
            File pluginsDirectory = new File(file.getParentFile(), "plugins");
            if (!pluginsDirectory.exists()) {
                throw new RuntimeException("No plugins directory found");
            }

            File[] jarFiles = pluginsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jarFiles != null) {
                URL[] urls = new URL[jarFiles.length];
                for (int i = 0; i < jarFiles.length; i++) {
                    try {
                        urls[i] = jarFiles[i].toURI().toURL();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                URLClassLoader childClassLoader = new URLClassLoader(urls, getClass().getClassLoader());

                StructurizrDslPlugin plugin = (StructurizrDslPlugin)Class.forName(fqn, true, childClassLoader).getDeclaredConstructor().newInstance();
                StructurizrDslPluginContext pluginContext = new StructurizrDslPluginContext(context.getWorkspace());

                plugin.run(pluginContext);
            } else {
                throw new RuntimeException("No JAR files found in " + pluginsDirectory.getCanonicalPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running plugin " + fqn + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}