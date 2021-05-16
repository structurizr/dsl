package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.io.File;
import java.util.List;
import java.util.Map;

final class DslParserContext extends DslContext {

    private boolean restricted;
    private File file;

    DslParserContext(File file, boolean restricted) {
        this.file = file;
        this.restricted = restricted;
    }

    File getFile() {
        return file;
    }

    boolean isRestricted() {
        return restricted;
    }

    void copyFrom(Map<String, Element> elements, Map<String, Relationship> relationships) {
        for (String identifier : elements.keySet()) {
            this.elements.put(identifier, elements.get(identifier));
        }

        for (String identifier : relationships.keySet()) {
            this.relationships.put(identifier, relationships.get(identifier));
        }
    }

}