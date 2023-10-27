package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefParserTests extends AbstractTests {

    private RefParser parser = new RefParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!ref", "name", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !ref <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierOrCanonicalNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("!extend"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !extend <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheReferencedElementCannotBeFound() {
        try {
            parser.parse(context(), tokens("!ref", "Person://User"));
            fail();
        } catch (Exception e) {
            assertEquals("An element/relationship identified by \"Person://User\" could not be found", e.getMessage());
        }
    }

    @Test
    void test_parse_FindsAnElementByCanonicalName() {
        Person user = workspace.getModel().addPerson("User");
        ModelItem element = parser.parse(context(), tokens("!ref", "Person://User"));

        assertSame(user, element);
    }

    @Test
    void test_parse_FindsAnElementByIdentifier() {
        Person user = workspace.getModel().addPerson("User");

        ModelDslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("user", user);
        context.setIdentifierRegister(register);

        ModelItem modelItem = parser.parse(context, tokens("!ref", "user"));
        assertSame(modelItem, user);
    }

    @Test
    void test_parse_FindsARelationshipByIdentifier() {
        Person user = workspace.getModel().addPerson("User");
        Relationship relationship = user.interactsWith(user, "Description");

        ModelDslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("rel", relationship);
        context.setIdentifierRegister(register);

        ModelItem modelItem = parser.parse(context, tokens("!ref", "rel"));
        assertSame(modelItem, relationship);
    }

}