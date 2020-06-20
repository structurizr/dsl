package com.structurizr.dsl;

import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutoLayoutParserTests extends AbstractTests {

    private AutoLayoutParser parser = new AutoLayoutParser();

    @Test
    void test_parse_EnablesAutoLayoutWithSomeDefaults() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getAutomaticLayout());
        parser.parse(context, tokens("autoLayout"));
        assertEquals(AutomaticLayout.Implementation.Graphviz, view.getAutomaticLayout().getImplementation());
        assertEquals(AutomaticLayout.RankDirection.TopBottom, view.getAutomaticLayout().getRankDirection());
        assertEquals(300, view.getAutomaticLayout().getRankSeparation());
        assertEquals(300, view.getAutomaticLayout().getNodeSeparation());
    }

    @Test
    void test_parse_EnablesAutoLayoutWithAValidRankDirection() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getAutomaticLayout());
        parser.parse(context, tokens("autoLayout", "lr"));
        assertEquals(AutomaticLayout.Implementation.Graphviz, view.getAutomaticLayout().getImplementation());
        assertEquals(AutomaticLayout.RankDirection.LeftRight, view.getAutomaticLayout().getRankDirection());
        assertEquals(300, view.getAutomaticLayout().getRankSeparation());
        assertEquals(300, view.getAutomaticLayout().getNodeSeparation());
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidRankDirectionIsSpecified() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parse(context, tokens("autoLayout", "hello"));
            fail();
        } catch (Exception e) {
            assertEquals("Valid rank directions are: tb|bt|lr|rl", e.getMessage());
        }
    }

    @Test
    void test_parse_EnablesAutoLayoutWithAValidRankSeparation() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getAutomaticLayout());
        parser.parse(context, tokens("autoLayout", "tb", "123"));
        assertEquals(AutomaticLayout.Implementation.Graphviz, view.getAutomaticLayout().getImplementation());
        assertEquals(AutomaticLayout.RankDirection.TopBottom, view.getAutomaticLayout().getRankDirection());
        assertEquals(123, view.getAutomaticLayout().getRankSeparation());
        assertEquals(300, view.getAutomaticLayout().getNodeSeparation());
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidRankSeparationIsSpecified() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parse(context, tokens("autoLayout", "tb", "hello"));
            fail();
        } catch (Exception e) {
            assertEquals("Rank separation must be positive integer in pixels", e.getMessage());
        }
    }

    @Test
    void test_parse_EnablesAutoLayoutWithAValidNodeSeparation() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        assertNull(view.getAutomaticLayout());
        parser.parse(context, tokens("autoLayout", "tb", "123", "456"));
        assertEquals(AutomaticLayout.Implementation.Graphviz, view.getAutomaticLayout().getImplementation());
        assertEquals(AutomaticLayout.RankDirection.TopBottom, view.getAutomaticLayout().getRankDirection());
        assertEquals(123, view.getAutomaticLayout().getRankSeparation());
        assertEquals(456, view.getAutomaticLayout().getNodeSeparation());
    }

    @Test
    void test_parse_ThrowsAnException_WhenAnInvalidNodeSeparationIsSpecified() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);

        try {
            parser.parse(context, tokens("autoLayout", "tb", "300", "hello"));
            fail();
        } catch (Exception e) {
            assertEquals("Node separation must be positive integer in pixels", e.getMessage());
        }
    }

}