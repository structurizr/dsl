package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Used to pass contextual information to DSL scripts when they are executed.
 */
public class StructurizrDslScriptContext {

    private final File dslFile;
    private final Workspace workspace;
    private final Map<String,String> parameters;

    /**
     * Creates a new instance.
     *
     * @param dslFile       a reference to the DSL file that loaded the script
     * @param workspace     the workspace
     * @param parameters    a map of name/value pairs representing parameters
     */
    public StructurizrDslScriptContext(File dslFile, Workspace workspace, Map<String,String> parameters) {
        this.dslFile = dslFile;
        this.workspace = workspace;
        this.parameters = parameters;
    }

    /**
     * Gets a reference to the DSL file that initiated this script context.
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