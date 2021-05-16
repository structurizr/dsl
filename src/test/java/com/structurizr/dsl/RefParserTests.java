package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefParserTests extends AbstractTests {

    private RefParser parser = new RefParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("ref", "name", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: ref <canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("ref"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: ref <canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNamedElementCannotBeFound() {
        try {
            parser.parse(context(), tokens("ref", "Person://User"));
            fail();
        } catch (Exception e) {
            assertEquals("Person://User could not be found", e.getMessage());
        }
    }

    @Test
    void test_parse_FindTheNamedElement() {
        Person user = workspace.getModel().addPerson("User");
        Element element = parser.parse(context(), tokens("ref", "Person://User"));

        assertSame(user, element);
    }

}