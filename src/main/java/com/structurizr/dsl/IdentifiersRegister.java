package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A register of elements and relationships that were created with an identifier in the DSL.
 */
public class IdentifiersRegister {

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\w[a-zA-Z0-9_-]*");

    private IdentifierScope identifierScope = IdentifierScope.Flat;

    private final Map<String, Element> elementsByIdentifier = new HashMap<>();

    private final Map<String, Relationship> relationshipsByIdentifier = new HashMap<>();

    IdentifiersRegister() {
    }

    /**
     * Gets the identifier scope in use (i.e. Flat or Hierarchical ... applies to elements only).
     *
     * @return      an IdentifierScope enum
     */
    public IdentifierScope getIdentifierScope() {
        return identifierScope;
    }

    /**
     * Sets the identifier scope  (i.e. Flat or Hierarchical ... applies to elements only).
     *
     * @param identifierScope       an IdentifierScope enum
     */
    public void setIdentifierScope(IdentifierScope identifierScope) {
        this.identifierScope = identifierScope;
    }

    /**
     * Gets the set of element identifiers.
     *
     * @return  a Set of String identifiers
     */
    public Set<String> getElementIdentifiers() {
        return elementsByIdentifier.keySet();
    }

    /**
     * Gets the set of relationship identifiers.
     *
     * @return  a Set of String identifiers
     */
    public Set<String> getRelationshipIdentifiers() {
        return relationshipsByIdentifier.keySet();
    }

    /**
     * Gets the element identified by the specified identifier.
     *
     * @param identifier        a String identifier
     * @return                  an Element, or null if one doesn't exist
     */
    public Element getElement(String identifier) {
        identifier = identifier.toLowerCase();
        return elementsByIdentifier.get(identifier);
    }

    /**
     * Registers an element with the given identifier.
     *
     * @param identifier        an identifier
     * @param element           an Element instance
     */
    public void register(String identifier, Element element) {
        if (element == null) {
            throw new IllegalArgumentException("An element must be specified");
        }

        if (StringUtils.isNullOrEmpty(identifier)) {
            identifier = UUID.randomUUID().toString();
        }

        identifier = identifier.toLowerCase();

        if (identifierScope == IdentifierScope.Hierarchical) {
            identifier = calculateHierarchicalIdentifier(identifier, element);
        }

        // check whether this element has already been registered with another identifier
        for (String id : elementsByIdentifier.keySet()) {
            Element e = elementsByIdentifier.get(id);

            if (e.equals(element) && !id.equals(identifier)) {
                if (id.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")) {
                    throw new RuntimeException("Please assign an identifier to \"" + element.getCanonicalName() + "\" before using it with !ref");
                } else {
                    throw new RuntimeException("The element is already registered with an identifier of \"" + id + "\"");
                }
            }
        }

        Element e = elementsByIdentifier.get(identifier);
        Relationship r = relationshipsByIdentifier.get(identifier);

        if ((e == null && r == null) || (e == element)) {
            elementsByIdentifier.put(identifier, element);
        } else {
            throw new RuntimeException("The identifier \"" + identifier + "\" is already in use");
        }
    }

    /**
     * Gets the relationship identified by the specified identifier.
     *
     * @param identifier        a String identifier
     * @return                  a Relationship, or null if one doesn't exist
     */
    public Relationship getRelationship(String identifier) {
        identifier = identifier.toLowerCase();
        return relationshipsByIdentifier.get(identifier);
    }

    /**
     * Registers a relationship with the given identifier.
     *
     * @param identifier        an identifier
     * @param relationship      a Relationship instance
     */
    public void register(String identifier, Relationship relationship) {
        if (relationship == null) {
            throw new IllegalArgumentException("A relationship must be specified");
        }

        if (StringUtils.isNullOrEmpty(identifier)) {
            identifier = UUID.randomUUID().toString();
        }

        identifier = identifier.toLowerCase();

        Element e = elementsByIdentifier.get(identifier);
        Relationship r = relationshipsByIdentifier.get(identifier);

        if ((e == null && r == null) || (r == relationship)) {
            relationshipsByIdentifier.put(identifier, relationship);
        } else {
            throw new RuntimeException("The identifier \"" + identifier + "\" is already in use");
        }
    }

    private String calculateHierarchicalIdentifier(String identifier, Element element) {
        if (element.getParent() == null) {
            if (element instanceof DeploymentNode) {
                DeploymentNode dn = (DeploymentNode)element;
                return findIdentifier(new DeploymentEnvironment(dn.getEnvironment())) + "." + identifier;
            } else {
                return identifier;
            }
        } else {
            return findIdentifier(element.getParent()) + "." + identifier;
        }
    }

    /**
     * Finds the identifier used when defining an element.
     *
     * @param element       an Element instance
     * @return  a String identifier (could be null if no identifier was explicitly specified)
     */
    public String findIdentifier(Element element) {
        if (elementsByIdentifier.containsValue(element)) {
            for (String identifier : elementsByIdentifier.keySet()) {
                Element e = elementsByIdentifier.get(identifier);

                if (e.equals(element)) {
                    return identifier;
                }
            }
        }

        return null;
    }

    /**
     * Finds the identifier used when defining a relationship.
     *
     * @param relationship      a Relationship instance
     * @return  a String identifier (could be null if no identifier was explicitly specified, or for implied relationships)
     */
    public String findIdentifier(Relationship relationship) {
        if (relationshipsByIdentifier.containsValue(relationship)) {
            for (String identifier : relationshipsByIdentifier.keySet()) {
                Relationship r = relationshipsByIdentifier.get(identifier);

                if (r.equals(relationship)) {
                    return identifier;
                }
            }
        }

        return null;
    }

    void validateIdentifierName(String identifier) {
        if (identifier.startsWith("-")) {
            throw new RuntimeException("Identifiers cannot start with a - character");
        }

        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new RuntimeException("Identifiers can only contain the following characters: a-zA-Z0-9_-");
        }
    }

}