package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DeploymentView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentViewParserTests extends AbstractTests {

    private DeploymentViewParser parser = new DeploymentViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment", "identifier", "environment", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: deployment <*|software system identifier> <environment> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deployment <*|software system identifier> <environment> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDeploymentEnvironmentIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("deployment", "*"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deployment <*|software system identifier> <environment> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheEnvironmentDoesNotExist() {
        DslContext context = context();

        try {
            parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The environment \"Live\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotDefined() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

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
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" is not a software system", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScope() {
        context().getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        parser.parse(context(), tokens("deployment", "*", "Live"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("Deployment-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScopeAndKey_ViaEnvironmentName() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        parser.parse(context, tokens("deployment", "*", "Live", "key"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
        assertEquals("Live", views.get(0).getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScopeAndKey_ViaEnvironmentReference() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("env", new DeploymentEnvironment("Live"));
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("deployment", "*", "env", "key"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
        assertEquals("Live", views.get(0).getEnvironment());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithNoScopeAndKeyAndDescription() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        parser.parse(context, tokens("deployment", "*", "Live", "key", "Description"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertNull(views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithSoftwareSystemScope() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("deployment", "softwareSystem", "Live"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("Deployment-001", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithSoftwareSystemScopeAndKey() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getSoftwareSystem());
    }

    @Test
    void test_parse_CreatesADeploymentViewWithSoftwareSystemScopeAndKeyAndDescription() {
        DslContext context = context();
        context.getWorkspace().getModel().addDeploymentNode("Live", "Deployment Node", "Description", "Technology");

        IdentifiersRegister elements = new IdentifiersRegister();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("deployment", "softwareSystem", "Live", "key", "Description"));
        List<DeploymentView> views = new ArrayList<>(this.views.getDeploymentViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertSame(softwareSystem, views.get(0).getSoftwareSystem());
    }

}