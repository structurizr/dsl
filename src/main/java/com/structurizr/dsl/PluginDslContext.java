package com.structurizr.dsl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

class PluginDslContext extends DslContext {

    private String fullyQualifiedClassName;
    private File pluginsDirectory;
    private Map<String,String> parameters = new HashMap<>();

    PluginDslContext(String fullyQualifiedClassName, File scriptDirectory) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
        this.pluginsDirectory = new File(scriptDirectory, "plugins");
    }

    void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    void end() {
        String fqn = fullyQualifiedClassName;
        try {
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
                StructurizrDslPluginContext pluginContext = new StructurizrDslPluginContext(getWorkspace(), parameters);
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