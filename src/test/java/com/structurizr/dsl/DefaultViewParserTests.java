package com.structurizr.dsl;

import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultViewParserTests extends AbstractTests {

    private final DefaultViewParser parser = new DefaultViewParser();

    @Test
    void test_parse_SetsTheDefaultView() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(context.getWorkspace().getViews().getConfiguration().getDefaultView());
        parser.parse(context);
        assertEquals("key", context.getWorkspace().getViews().getConfiguration().getDefaultView());
    }

}