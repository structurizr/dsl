package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PluginDslContextTests extends AbstractTests {

    @Test
    void test_end_ThrowsAnException_WhenThePluginClassDoesNotExist() {
        try {
            PluginDslContext context = new PluginDslContext("com.structurizr.TestPlugin", new File("src/test/dsl"), null);
            context.end();
            fail();
        } catch (Exception e) {
            assertEquals("Error running plugin com.structurizr.TestPlugin, caused by java.lang.ClassNotFoundException: com.structurizr.TestPlugin", e.getMessage());
        }
    }

}