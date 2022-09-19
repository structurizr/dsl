package com.structurizr.dsl;

import com.structurizr.model.Enterprise;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnterpriseParserTests extends AbstractTests {

    private EnterpriseParser parser = new EnterpriseParser();

    @Test
    void test_parse_SetsTheEnterpriseName_WhenItHasNotBeenSet() {
        assertNull(workspace.getModel().getEnterprise());
        parser.parse(context(), tokens("enterprise", "New Name"));
        assertEquals("New Name", workspace.getModel().getEnterprise().getName());
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheEnterpriseNameHasAlreadyBeenSet() {
        workspace.getModel().setEnterprise(new Enterprise("My Enterprise"));
        try {
            parser.parse(context(), tokens("enterprise", "name"));
            fail();
        } catch (Exception e) {
            assertEquals("The name of the enterprise has already been set", e.getMessage());
        }
    }

    @Test
    void test_parse_DoesNothing_WhenANameIsSpecifiedButIsTheSameAsTheExistingEnterpriseName() {
        workspace.getModel().setEnterprise(new Enterprise("My Enterprise"));
        parser.parse(context(), tokens("My Enterprise"));

        assertEquals("My Enterprise", workspace.getModel().getEnterprise().getName());
    }

    @Test
    void test_parse_DoesNothing_WhenNoNameIsSpecified() {
        parser.parse(context(), tokens("enterprise"));

        assertNull(workspace.getModel().getEnterprise());
    }

    @Test
    void test_parse_DoesNothing_WhenNoNameIsSpecifiedAndTheEnterpriseNameHasAlreadyBeenSet() {
        workspace.getModel().setEnterprise(new Enterprise("My Enterprise"));
        parser.parse(context(), tokens("enterprise"));

        assertEquals("My Enterprise", workspace.getModel().getEnterprise().getName());
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("enterprise", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: enterprise [name]", e.getMessage());
        }
    }

}