package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DeploymentView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentViewParserTests extends AbstractTests {

    private DeploymentViewParser parser = new DeploymentViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deployment <*|software system identifier> <environment name> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDeploymentEnvironmentIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment", "*"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deployment <*|software system identifier> <environment name> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotDefined() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The software system \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystem() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addPerson("Name", "Description"));
        context.setElements(elements);

        try {
            parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" is not a software system", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScope() {
        parser.parse(context(), tokens("deployment", "*", "Live"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("001-Deployment", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScopeAndKey() {
        parser.parse(context(), tokens("deployment", "*", "Live", "key"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScopeAndKeyAndDescription() {
        parser.parse(context(), tokens("deployment", "*", "Live", "key", "Description"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithSoftwareSystemScope() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithSoftwareSystemScopeAndDescription() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key", "Description"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getSoftwareSystem());
    }

}