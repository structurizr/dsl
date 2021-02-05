package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeploymentViewAnimationStepParserTests extends AbstractTests {

    private DeploymentViewAnimationStepParser parser = new DeploymentViewAnimationStepParser();

    @Test
    void test_parseExplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((DeploymentViewDslContext)null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier> [identifier...]", e.getMessage());
        }
    }

    @Test
    void test_parseImplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((DeploymentViewAnimationDslContext)null, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier> [identifier...]", e.getMessage());
        }
    }

}