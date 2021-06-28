package com.structurizr.dsl;

import java.io.File;

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

    void copyFrom(IdentifersRegister identifersRegister) {
        for (String identifier : identifersRegister.getElementIdentifiers()) {
            this.identifersRegister.register(identifier, identifersRegister.getElement(identifier));
        }

        for (String identifier : identifersRegister.getRelationshipIdentifiers()) {
            this.identifersRegister.register(identifier, identifersRegister.getRelationship(identifier));
        }
    }

}