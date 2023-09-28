package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IdentifierRegisterTests extends AbstractTests {

    private final IdentifiersRegister register = new IdentifiersRegister();

    @Test
    void test_validateIdentifierName() {
        new IdentifiersRegister().validateIdentifierName("a");
        new IdentifiersRegister().validateIdentifierName("abc");
        new IdentifiersRegister().validateIdentifierName("ABC");
        new IdentifiersRegister().validateIdentifierName("softwaresystem");
        new IdentifiersRegister().validateIdentifierName("SoftwareSystem");
        new IdentifiersRegister().validateIdentifierName("123456");
        new IdentifiersRegister().validateIdentifierName("_softwareSystem");
        new IdentifiersRegister().validateIdentifierName("SoftwareSystem-1");

        try {
            new IdentifiersRegister().validateIdentifierName("-softwareSystem");
            fail();
        } catch (Exception e) {
            assertEquals("Identifiers cannot start with a - character", e.getMessage());
        }

        try {
            new IdentifiersRegister().validateIdentifierName("SoftwareSystém");
            fail();
        } catch (Exception e) {
            assertEquals("Identifiers can only contain the following characters: a-zA-Z0-9_-", e.getMessage());
        }
    }

    @Test
    void test_register_ThrowsAnException_WhenTheElementHasAlreadyBeenRegisteredWithADifferentIdentifier() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        try {
            register.register("a", softwareSystem);
            register.register("x", softwareSystem);
            fail();
        } catch (Exception e) {
            assertEquals("The element is already registered with an identifier of \"a\"", e.getMessage());
        }
    }

    @Test
    void test_register_ThrowsAnException_WhenTheElementHasAlreadyBeenRegisteredWithAnInternalIdentifier() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        try {
            register.register("", softwareSystem);
            register.register("x", softwareSystem);
            fail();
        } catch (Exception e) {
            assertEquals("Please assign an identifier to \"SoftwareSystem://Software System\" before using it with !ref", e.getMessage());
        }
    }

}