package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CustomViewAnimationStepParserTests extends AbstractTests {

    private CustomViewAnimationStepParser parser = new CustomViewAnimationStepParser();

    @Test
    void test_parseExplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((CustomViewDslContext)null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier> [identifier...]", e.getMessage());
        }
    }

    @Test
    void test_parseImplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((CustomViewAnimationDslContext) null, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier> [identifier...]", e.getMessage());
        }
    }

}