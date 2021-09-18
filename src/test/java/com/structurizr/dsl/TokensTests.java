package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokensTests {

    @Test
    void test_get_HandlesEscapedDoubleQuotes() {
        Tokens tokens = new Tokens(Collections.singletonList("Hello \\\"World\\\""));
        assertEquals("Hello \"World\"", tokens.get(0));
    }

    @Test
    void test_get_HandlesEscapedNewlines() {
        Tokens tokens = new Tokens(Collections.singletonList("Hello\\nWorld"));
        assertEquals("Hello\nWorld", tokens.get(0));
    }

}
