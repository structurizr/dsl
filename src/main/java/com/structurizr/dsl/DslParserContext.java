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

    void copyFrom(IdentifiersRegister identifersRegister) {
        for (String identifier : identifersRegister.getElementIdentifiers()) {
            this.identifiersRegister.register(identifier, identifersRegister.getElement(identifier));
        }

        for (String identifier : identifersRegister.getRelationshipIdentifiers()) {
            this.identifiersRegister.register(identifier, identifersRegister.getRelationship(identifier));
        }
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}