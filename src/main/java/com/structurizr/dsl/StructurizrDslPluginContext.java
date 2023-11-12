package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Used to pass contextual information to DSL plugins when they are executed.
 */
public class StructurizrDslPluginContext {

    private final StructurizrDslParser dslParser;
    private final File dslFile;
    private final Workspace workspace;
    private final Map<String,String> parameters;

    /**
     * Creates a new instance.
     *
     * @param dslParser     a reference to the DSL parser that loaded the plugin
     * @param dslFile       a reference to the DSL file that loaded the plugin
     * @param workspace     the workspace
     * @param parameters    a map of name/value pairs representing parameters
     */
    public StructurizrDslPluginContext(StructurizrDslParser dslParser, File dslFile, Workspace workspace, Map<String,String> parameters) {
        this.dslParser = dslParser;
        this.dslFile = dslFile;
        this.workspace = workspace;
        this.parameters = parameters;
    }

    /**
     * Gets a reference to the DSL parser that initiated this plugin context.
     *
     * @return  a StructurizrDslParser instance
     */
    public StructurizrDslParser getDslParser() {
        return dslParser;
    }

    /**
     * Gets a reference to the DSL file that initiated this plugin context.
     *
     * @return  a File instance
     */
    public File getDslFile() {
        return dslFile;
    }

    /**
     * Gets the current workspace.
     *
     * @return  a Workspace instance
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Gets the named parameter.
     *
     * @param name      the parameter name
     * @return      the parameter value (null if unset)
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Gets the named parameter, with a default value if unset.
     *
     * @param name          the parameter name
     * @param defaultValue  the default value
     * @return      the parameter value, or defaultValue if unset
     */
    public String getParameter(String name, String defaultValue) {
        String value = parameters.get(name);

        if (StringUtils.isNullOrEmpty(value)) {
            value = defaultValue;
        }

        return value;
    }

}