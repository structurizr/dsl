package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IdentifierRegisterTests extends AbstractTests {

    private IdentifiersRegister register = new IdentifiersRegister();

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