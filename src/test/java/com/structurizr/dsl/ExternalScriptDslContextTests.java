package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ExternalScriptDslContextTests extends AbstractTests {

    @Test
    void test_parseExternal_RunsTheScript_WhenAValidScriptFilenameIsSpecified() {
        ExternalScriptDslContext context = new ExternalScriptDslContext(new WorkspaceDslContext(), new File("src/test/dsl/workspace.dsl"), "test.kts");
        context.setWorkspace(workspace);
        context.end();

        assertNotNull(workspace.getModel().getPersonWithName("Kotlin"));
    }

}