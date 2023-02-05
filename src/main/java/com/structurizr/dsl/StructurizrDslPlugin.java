package com.structurizr.dsl;

/**
 * An interface implemented by DSL plugins.
 */
public interface StructurizrDslPlugin {

    /**
     * Called to execute the plugin.
     *
     * @param context       a StructurizrDslPluginContext instance
     */
    void run(StructurizrDslPluginContext context);

}