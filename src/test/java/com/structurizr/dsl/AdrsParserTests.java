package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AdrsParserTests extends AbstractTests {

    private AdrsParser parser = new AdrsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new WorkspaceDslContext(), null, tokens("adrs", "path", "fqn", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !adrs <path> <fqn>", e.getMessage());
        }
    }

}