package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StaticViewAnimationStepParserTests extends AbstractTests {

    private StaticViewAnimationStepParser parser = new StaticViewAnimationStepParser();

    @Test
    void test_parse_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse(null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier> [identifier...]", e.getMessage());
        }
    }

}