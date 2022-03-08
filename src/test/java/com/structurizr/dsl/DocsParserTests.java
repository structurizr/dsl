package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DocsParserTests extends AbstractTests {

    private DocsParser parser = new DocsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new WorkspaceDslContext(), null, tokens("docs", "path", "fqn", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !docs <path> <fqn>", e.getMessage());
        }
    }

}