package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class InlineScriptDslContextTests extends AbstractTests {

    @Test
    void test_end_ThrowsAnException_WhenAnUnsupportedLanguageIsSpecified() {
        try {
            InlineScriptDslContext context = new InlineScriptDslContext(new WorkspaceDslContext(), new File("workspace.dsl"), "java");
            context.end();
            fail();
        } catch (Exception e) {
            assertEquals("Error running inline script, caused by java.lang.RuntimeException: Unsupported scripting language \"java\"", e.getMessage());
        }
    }
    
}