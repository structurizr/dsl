package com.structurizr.dsl;

import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.DynamicView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewParserTests extends AbstractTests {

    private ViewParser parser = new ViewParser();

    @Test
    void test_parseTitle_ThrowsAnException_WhenNoTitleIsSpecified() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseTitle(context, tokens("title"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: title <title>", e.getMessage());
        }
    }

    @Test
    void test_parseTitle_ThrowsAnException_WhenTooManyTokensAreSpecified() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parseTitle(context, tokens("title", "hello", "world"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: title <title>", e.getMessage());
        }
    }

    @Test
    void test_parseTitle_SetsTheTitleOfAStaticView() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getTitle());
        parser.parseTitle(context, tokens("title", "A new title"));
        assertEquals("A new title", view.getTitle());
    }

    @Test
    void test_parseTitle_SetsTheTitleOfADynamicView() {
        DynamicView view = workspace.getViews().createDynamicView("key", "description");
        DynamicViewDslContext context = new DynamicViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getTitle());
        parser.parseTitle(context, tokens("title", "A new title"));
        assertEquals("A new title", view.getTitle());
    }

    @Test
    void test_parseTitle_SetsTheTitleOfADeploymentView() {
        DeploymentView view = workspace.getViews().createDeploymentView("key", "description");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getTitle());
        parser.parseTitle(context, tokens("title", "A new title"));
        assertEquals("A new title", view.getTitle());
    }

}