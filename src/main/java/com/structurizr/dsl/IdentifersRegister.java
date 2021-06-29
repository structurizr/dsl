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

class IdentifersRegister {

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\w+");

    private IdentifierScope identifierScope = IdentifierScope.Flat;

    private Map<String, Element> elementsByIdentifier = new HashMap<>();
    private Map<Element, String> identifiersByElement = new HashMap<>();

    private Map<String, Relationship> relationshipsByIdentifier = new HashMap<>();

    IdentifersRegister() {
    }

    IdentifierScope getIdentifierScope() {
        return identifierScope;
    }

    void setIdentifierScope(IdentifierScope identifierScope) {
        this.identifierScope = identifierScope;
    }

    Set<String> getElementIdentifiers() {
        return elementsByIdentifier.keySet();
    }

    Set<String> getRelationshipIdentifiers() {
        return relationshipsByIdentifier.keySet();
    }

    Element getElement(String identifier) {
        identifier = identifier.toLowerCase();
        return elementsByIdentifier.get(identifier);
    }

    void register(String identifier, Element element) {
        if (StringUtils.isNullOrEmpty(identifier)) {
            identifier = UUID.randomUUID().toString();
        }

        identifier = identifier.toLowerCase();

        if (identifierScope == IdentifierScope.Hierarchical) {
            identifier = calculateHierarchicalIdentifier(identifier, element);
        }

        Element e = elementsByIdentifier.get(identifier);
        Relationship r = relationshipsByIdentifier.get(identifier);

        if ((e == null && r == null) || (e == element)) {
            elementsByIdentifier.put(identifier, element);
            identifiersByElement.put(element, identifier);
        } else {
            throw new RuntimeException("The identifier \"" + identifier + "\" is already in use");
        }
    }

    Relationship getRelationship(String identifier) {
        return relationshipsByIdentifier.get(identifier);
    }

    void register(String identifier, Relationship relationship) {
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

    String findIdentifier(Element element) {
        return identifiersByElement.get(element);
    }

    void validateIdentifiername(String identifier) {
        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new RuntimeException("Identifiers can only contain the following characters: a-zA-Z_0-9");
        }
    }

}