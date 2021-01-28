package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.Shape;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ExampleTests extends AbstractTests {

    @Test
    void test_test() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/test.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
    }

    @Test
    void test_utf8() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/utf8.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("你好 Usér \uD83D\uDE42");
        assertNotNull(user);
    }

    @Test
    void test_gettingstarted() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/getting-started.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("Getting Started", workspace.getName());
        assertEquals("This is a model of my software system.", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system.", softwareSystem.getDescription());

        assertEquals(1, workspace.getModel().getRelationships().size());
        Relationship relationship = user.getRelationships().iterator().next();
        assertEquals("Uses", relationship.getDescription());
        assertSame(softwareSystem, relationship.getDestination());

        assertEquals(1, views.getViews().size());
        assertEquals(1, views.getSystemContextViews().size());
        SystemContextView view = views.getSystemContextViews().iterator().next();
        assertEquals("SystemContext", view.getKey());
        assertEquals("An example of a System Context diagram.", view.getDescription());
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        assertEquals(2, views.getConfiguration().getStyles().getElements().size());
        ElementStyle personStyle = views.getConfiguration().getStyles().getElements().stream().filter(es -> es.getTag().equals("Person")).findFirst().get();
        assertEquals(Shape.Person, personStyle.getShape());
        assertEquals("#08427b", personStyle.getBackground());
        assertEquals("#ffffff", personStyle.getColor());

        ElementStyle softwareSystemStyle = views.getConfiguration().getStyles().getElements().stream().filter(es -> es.getTag().equals("Software System")).findFirst().get();
        assertEquals("#1168bd", softwareSystemStyle.getBackground());
        assertEquals("#ffffff", softwareSystemStyle.getColor());
    }

    @Test
    void test_aws() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/amazon-web-services.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(13, workspace.getModel().getElements().size());
        assertEquals(0, workspace.getModel().getPeople().size());
        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        assertEquals(2, workspace.getModel().getSoftwareSystemWithName("Spring PetClinic").getContainers().size());
        assertEquals(1, workspace.getModel().getDeploymentNodes().size());
        assertEquals(6, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(4, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(0, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(1, workspace.getViews().getDeploymentViews().size());

        assertEquals(10, workspace.getViews().getDeploymentViews().iterator().next().getElements().size());
        assertEquals(3, workspace.getViews().getDeploymentViews().iterator().next().getRelationships().size());

        assertEquals(3, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_bigbankplc() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/big-bank-plc.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(51, workspace.getModel().getElements().size());
        assertEquals(3, workspace.getModel().getPeople().size());
        assertEquals(4, workspace.getModel().getSoftwareSystems().size());
        assertEquals(5, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainers().size());
        assertEquals(6, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainerWithName("API Application").getComponents().size());
        assertEquals(5, workspace.getModel().getDeploymentNodes().size());
        assertEquals(21, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).count());
        assertEquals(10, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(42, workspace.getModel().getRelationships().size());

        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(1, workspace.getViews().getContainerViews().size());
        assertEquals(1, workspace.getViews().getComponentViews().size());
        assertEquals(1, workspace.getViews().getDynamicViews().size());
        assertEquals(2, workspace.getViews().getDeploymentViews().size());

        assertEquals(7, workspace.getViews().getSystemLandscapeViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getElements().size());
        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getRelationships().size());

        assertEquals(8, workspace.getViews().getContainerViews().iterator().next().getElements().size());
        assertEquals(10, workspace.getViews().getContainerViews().iterator().next().getRelationships().size());

        assertEquals(11, workspace.getViews().getComponentViews().iterator().next().getElements().size());
        assertEquals(13, workspace.getViews().getComponentViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getDynamicViews().iterator().next().getElements().size());
        assertEquals(6, workspace.getViews().getDynamicViews().iterator().next().getRelationships().size());

        assertEquals(13, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getElements().size());
        assertEquals(4, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getRelationships().size());

        assertEquals(20, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getElements().size());
        assertEquals(7, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getRelationships().size());

        assertEquals(10, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertNull(workspace.getViews().getConfiguration().getThemes());
    }

    @Test
    void test_frs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/financial-risk-system.dsl"));


        Workspace workspace = parser.getWorkspace();

        assertEquals(9, workspace.getModel().getElements().size());
        assertEquals(2, workspace.getModel().getPeople().size());
        assertEquals(7, workspace.getModel().getSoftwareSystems().size());
        assertEquals(0, workspace.getModel().getDeploymentNodes().size());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(9, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(0, workspace.getViews().getDeploymentViews().size());

        assertEquals(9, workspace.getViews().getSystemContextViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemContextViews().iterator().next().getRelationships().size());

        assertEquals(5, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(4, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertNull(workspace.getViews().getConfiguration().getThemes());
    }

    @Test
    void test_include() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/include.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("Getting Started", workspace.getName());
        assertEquals("This is a model of my software system.", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system, code-named \"X\".", softwareSystem.getDescription());

        assertEquals(1, workspace.getModel().getRelationships().size());
        Relationship relationship = user.getRelationships().iterator().next();
        assertEquals("Uses", relationship.getDescription());
        assertSame(softwareSystem, relationship.getDestination());

        assertEquals(1, views.getViews().size());
        assertEquals(1, views.getSystemContextViews().size());
        SystemContextView view = views.getSystemContextViews().iterator().next();
        assertEquals("SystemContext", view.getKey());
        assertEquals("An example of a System Context diagram.", view.getDescription());
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        assertEquals(2, views.getConfiguration().getStyles().getElements().size());
        ElementStyle personStyle = views.getConfiguration().getStyles().getElements().stream().filter(es -> es.getTag().equals("Person")).findFirst().get();
        assertEquals(Shape.Person, personStyle.getShape());
        assertEquals("#08427b", personStyle.getBackground());
        assertEquals("#ffffff", personStyle.getColor());

        ElementStyle softwareSystemStyle = views.getConfiguration().getStyles().getElements().stream().filter(es -> es.getTag().equals("Software System")).findFirst().get();
        assertEquals("#1168bd", softwareSystemStyle.getBackground());
        assertEquals("#ffffff", softwareSystemStyle.getColor());
    }

    @Test
    void test_include_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        try {
            // this will fail, because the model include will be ignored
            parser.parse(new File("examples/include.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("The software system \"softwareSystem\" does not exist at line 8: systemContext softwareSystem \"SystemContext\" \"An example of a System Context diagram.\" {", e.getMessage());
        }
    }

}