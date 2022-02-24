package com.structurizr.dsl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

class PluginDslContext extends DslContext {

    private final String fullyQualifiedClassName;
    private final File pluginsDirectory;
    private final Map<String,String> parameters = new HashMap<>();

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
            URL[] urls = new URL[0];

            if (pluginsDirectory.exists()) {
                File[] jarFiles = pluginsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
                if (jarFiles != null) {
                    urls = new URL[jarFiles.length];
                    for (int i = 0; i < jarFiles.length; i++) {
                        try {
                            urls[i] = jarFiles[i].toURI().toURL();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            URLClassLoader childClassLoader = new URLClassLoader(urls, getClass().getClassLoader());

            StructurizrDslPlugin plugin = (StructurizrDslPlugin)Class.forName(fqn, true, childClassLoader).getDeclaredConstructor().newInstance();
            StructurizrDslPluginContext pluginContext = new StructurizrDslPluginContext(getWorkspace(), parameters);
            plugin.run(pluginContext);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running plugin " + fqn + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}