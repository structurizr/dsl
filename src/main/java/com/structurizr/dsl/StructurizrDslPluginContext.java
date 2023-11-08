package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Used to pass contextual information to DSL plugins when they are executed.
 */
public class StructurizrDslPluginContext {

    private final File dslFile;
    private final Workspace workspace;
    private final Map<String,String> parameters;
    private IdentifiersRegister identifiersRegister = new IdentifiersRegister();

    /**
     * Creates a new instance.
     *
     * @param dslFile       a reference to the DSL file that loaded the plugin
     * @param workspace     the workspace
     * @param parameters    a map of name/value pairs representing parameters
     */
    public StructurizrDslPluginContext(File dslFile, Workspace workspace, Map<String,String> parameters) {
        this.dslFile = dslFile;
        this.workspace = workspace;
        this.parameters = parameters;
    }

    /**
     * Sets the IdentifiersRegister that can be used for registering identifiers and associated elements.
     *
     * @param identifiersRegister The IdentifiersRegister to be used.
     */
    public void setIdentifierRegister(IdentifiersRegister identifiersRegister) {
        this.identifiersRegister = identifiersRegister;
    }

    /**
     * Gets the element identified by the specified identifier.
     *
     * @param identifier        a String identifier
     * @return                  an Element, or null if one doesn't exist
     */
    public Element getElement(String identifier) {
        return identifiersRegister.getElement(identifier);
    }

    /**
     * Gets the relationship identified by the specified identifier.
     *
     * @param identifier        a String identifier
     * @return                  a Relationship, or null if one doesn't exist
     */
    public Relationship getRelationship(String identifier) {
        return identifiersRegister.getRelationship(identifier);
    }

    /**
     * Registers an identifier and its associated Element in the IdentifiersRegister.
     *
     * @param identifier The identifier to register.
     * @param element    The Element associated with the identifier.
     */
    public void register(String identifier, Element element){
        identifiersRegister.register(identifier, element);
    }

    /**
     * Registers an identifier and its associated Relationship in the IdentifiersRegister.
     *
     * @param identifier The identifier to register.
     * @param relationship    The Relationship associated with the identifier.
     */
    public void register(String identifier, Relationship relationship) {
        identifiersRegister.register(identifier, relationship);
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