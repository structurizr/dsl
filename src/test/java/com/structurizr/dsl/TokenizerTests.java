package com.structurizr.dsl;

import com.structurizr.model.Location;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTests extends AbstractTests {

    @Test
    void tokenize_ReturnsASingleToken_WhenTheLineIsOneUnquotedToken() {
        List<String> tokens = new Tokenizer().tokenize("Hello");
        assertEquals(1, tokens.size());
        assertEquals("Hello", tokens.get(0));
    }

    @Test
    void tokenize_ReturnsASingleToken_WhenTheLineIsOneUnquotedTokenWithEscapedQuoteCharacters() {
        List<String> tokens = new Tokenizer().tokenize("\"Hello \\\"World\\\"\"");
        assertEquals(1, tokens.size());
        assertEquals("Hello \\\"World\\\"", tokens.get(0));
    }

    @Test
    void tokenize_ReturnsASingleToken_WhenTheLineIsTwoUnquotedTokens() {
        List<String> tokens = new Tokenizer().tokenize("Hello World");
        assertEquals(2, tokens.size());
        assertEquals("Hello", tokens.get(0));
        assertEquals("World", tokens.get(1));
    }

    @Test
    void tokenize_ReturnsTwoTokens_WhenTheLineIsOneQuotedToken() {
        List<String> tokens = new Tokenizer().tokenize("\"Hello World\"");
        assertEquals(1, tokens.size());
        assertEquals("Hello World", tokens.get(0));
    }

    @Test
    void tokenize_ReturnsTwoTokens_WhenTheLineIsTwoQuotedTokens() {
        List<String> tokens = new Tokenizer().tokenize("\"Hello\" \"World\"");
        assertEquals(2, tokens.size());
        assertEquals("Hello", tokens.get(0));
        assertEquals("World", tokens.get(1));
    }

    @Test
    void tokenize_ReturnsTokens_WhenTheLineIsSeveralQuotedTokens() {
        List<String> tokens = new Tokenizer().tokenize("user = person \"User\"");
        assertEquals(4, tokens.size());
        assertEquals("user", tokens.get(0));
        assertEquals("=", tokens.get(1));
        assertEquals("person", tokens.get(2));
        assertEquals("User", tokens.get(3));
    }

    @Test
    void tokenize_ReturnsASingleToken_WhenTheLineIncludesTabCharacters() {
        List<String> tokens = new Tokenizer().tokenize("\t\tuser\t=\tperson\t\"User\"");
        assertEquals(4, tokens.size());
        assertEquals("user", tokens.get(0));
        assertEquals("=", tokens.get(1));
        assertEquals("person", tokens.get(2));
        assertEquals("User", tokens.get(3));
    }

}