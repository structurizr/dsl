package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImpliedRelationshipsParserTests extends AbstractTests {

    private ImpliedRelationshipsParser parser = new ImpliedRelationshipsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!impliedRelationships", "boolean", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !impliedRelationships <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoFlagIsSpecified() {
        try {
            parser.parse(context(), tokens("!impliedRelationships"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !impliedRelationships <true|false>", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheStrategy_WhenFalseIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "false"));

        assertEquals("com.structurizr.model.DefaultImpliedRelationshipsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenTrueIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "true"));

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

}