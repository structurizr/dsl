package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DslTests extends AbstractTests {

    @Test
    void test_test() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/test.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
        assertEquals("Organisation - Group", parser.getWorkspace().getModel().getEnterprise().getName());
    }

    @Test
    void test_utf8() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/utf8.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("你好 Usér \uD83D\uDE42");
        assertNotNull(user);
    }

    @Test
    void test_gettingstarted() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/getting-started.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");

        assertEquals(1, workspace.getModel().getRelationships().size());
        Relationship relationship = user.getRelationships().iterator().next();
        assertEquals("Uses", relationship.getDescription());
        assertSame(softwareSystem, relationship.getDestination());

        assertEquals(1, views.getViews().size());
        assertEquals(1, views.getSystemContextViews().size());
        SystemContextView view = views.getSystemContextViews().iterator().next();
        assertEquals("SoftwareSystem-SystemContext", view.getKey());
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_aws() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/amazon-web-services.dsl"));

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

        DeploymentView deploymentView = workspace.getViews().getDeploymentViews().iterator().next();
        assertEquals(10, deploymentView.getElements().size());
        assertEquals(3, deploymentView.getRelationships().size());
        assertEquals(4, deploymentView.getAnimations().size());

        assertEquals(3, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_awsLocal() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/amazon-web-services-local.dsl"));

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

        DeploymentView deploymentView = workspace.getViews().getDeploymentViews().iterator().next();
        assertEquals(10, deploymentView.getElements().size());
        assertEquals(3, deploymentView.getRelationships().size());
        assertEquals(4, deploymentView.getAnimations().size());

        assertEquals(3, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_bigbankplc() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/big-bank-plc.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(Location.External, workspace.getModel().getPersonWithName("Personal Banking Customer").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Customer Service Staff").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Back Office Staff").getLocation());

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

        assertEquals(11, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);

        assertEquals(0, workspace.getDocumentation().getSections().size());
        assertEquals(0, workspace.getDocumentation().getDecisions().size());
    }

    @Test
    void test_bigbankplc_systemlandscape() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/big-bank-plc/system-landscape.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(Location.External, workspace.getModel().getPersonWithName("Personal Banking Customer").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Customer Service Staff").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Back Office Staff").getLocation());

        assertEquals(7, workspace.getModel().getElements().size());
        assertEquals(3, workspace.getModel().getPeople().size());
        assertEquals(4, workspace.getModel().getSoftwareSystems().size());

        assertEquals(9, workspace.getModel().getRelationships().size());

        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(0, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(0, workspace.getViews().getDeploymentViews().size());

        assertEquals(7, workspace.getViews().getSystemLandscapeViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_bigbankplc_internetbankingsystem() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/big-bank-plc/internet-banking-system.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(Location.External, workspace.getModel().getPersonWithName("Personal Banking Customer").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Customer Service Staff").getLocation());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Back Office Staff").getLocation());

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

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(1, workspace.getViews().getContainerViews().size());
        assertEquals(1, workspace.getViews().getComponentViews().size());
        assertEquals(1, workspace.getViews().getDynamicViews().size());
        assertEquals(2, workspace.getViews().getDeploymentViews().size());

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

        assertEquals(11, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);

        assertEquals(4, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getDocumentation().getSections().size());
        assertEquals(1, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getDocumentation().getDecisions().size());
    }

    @Test
    void test_frs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/financial-risk-system.dsl"));


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

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_includeLocalFile() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/include-file.dsl"));

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

        assertEquals("workspace \"Getting Started\" \"This is a model of my software system.\" {\n" +
                "\n" +
                "    model {\n" +
                "user = person \"User\" \"A user of my software system.\"\n" +
                "softwareSystem = softwareSystem \"Software System\" \"My software system, code-named \\\"X\\\".\"\n" +
                "\n" +
                "user -> softwareSystem \"Uses\"\n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext softwareSystem \"SystemContext\" \"An example of a System Context diagram.\" {\n" +
                "            include *\n" +
                "            autoLayout\n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Software System\" {\n" +
                "                background #1168bd\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "            element \"Person\" {\n" +
                "                shape person\n" +
                "                background #08427b\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n", new String(Base64.getDecoder().decode(workspace.getProperties().get("structurizr.dsl"))));
    }

    @Test
    void test_includeLocalDirectory() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/include-directory.dsl"));

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

        assertEquals("workspace \"Getting Started\" \"This is a model of my software system.\" {\n" +
                "\n" +
                "    model {\n" +
                "user = person \"User\" \"A user of my software system.\"\n" +
                "softwareSystem = softwareSystem \"Software System\" \"My software system, code-named \\\"X\\\".\"\n" +
                "user -> softwareSystem \"Uses\"\n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext softwareSystem \"SystemContext\" \"An example of a System Context diagram.\" {\n" +
                "            include *\n" +
                "            autoLayout\n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Software System\" {\n" +
                "                background #1168bd\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "            element \"Person\" {\n" +
                "                shape person\n" +
                "                background #08427b\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n", new String(Base64.getDecoder().decode(workspace.getProperties().get("structurizr.dsl"))));
    }

    @Test
    void test_includeUrl() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/include-url.dsl"));

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

        assertEquals("workspace \"Getting Started\" \"This is a model of my software system.\" {\n" +
                "\n" +
                "    model {\n" +
                "user = person \"User\" \"A user of my software system.\"\n" +
                "softwareSystem = softwareSystem \"Software System\" \"My software system, code-named \\\"X\\\".\"\n" +
                "\n" +
                "user -> softwareSystem \"Uses\"\n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext softwareSystem \"SystemContext\" \"An example of a System Context diagram.\" {\n" +
                "            include *\n" +
                "            autoLayout\n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Software System\" {\n" +
                "                background #1168bd\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "            element \"Person\" {\n" +
                "                shape person\n" +
                "                background #08427b\n" +
                "                color #ffffff\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n", new String(Base64.getDecoder().decode(workspace.getProperties().get("structurizr.dsl"))));
    }

    @Test
    void test_include_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        try {
            // this will fail, because the model include will be ignored
            parser.parse(new File("src/test/dsl/include-file.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("The software system \"softwareSystem\" does not exist at line 8: systemContext softwareSystem \"SystemContext\" \"An example of a System Context diagram.\" {", e.getMessage());
        }
    }

    @Test
    void test_extendWorkspaceFromJsonFile() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/extend/extend-workspace-from-json-file.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("A new name", workspace.getName());
        assertEquals("A new description", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system.", softwareSystem.getDescription());

        assertEquals(1, softwareSystem.getContainers().size());
        assertEquals("Web Application", softwareSystem.getContainers().iterator().next().getName());

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
    void test_extendWorkspaceFromJsonUrl() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/extend/extend-workspace-from-json-url.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("A new name", workspace.getName());
        assertEquals("A new description", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system.", softwareSystem.getDescription());

        assertEquals(1, softwareSystem.getContainers().size());
        assertEquals("Web Application", softwareSystem.getContainers().iterator().next().getName());

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
    void test_extendWorkspaceFromJsonFile_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        try {
            // this will fail, because the model import will be ignored
            parser.parse(new File("src/test/dsl/extend/extend-workspace-from-json-file.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Cannot import workspace from a file when running in restricted mode at line 1: workspace extends workspace.json {", e.getMessage());
        }
    }

    @Test
    void test_extendWorkspaceFromDslFile() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/extend/extend-workspace-from-dsl-file.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("A new name", workspace.getName());
        assertEquals("A new description", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system.", softwareSystem.getDescription());

        assertEquals(1, softwareSystem.getContainers().size());
        assertEquals("Web Application", softwareSystem.getContainers().iterator().next().getName());

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
    void test_extendWorkspaceFromDslUrl() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/extend/extend-workspace-from-dsl-url.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals("A new name", workspace.getName());
        assertEquals("A new description", workspace.getDescription());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");
        assertEquals("A user of my software system.", user.getDescription());

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        assertEquals("My software system.", softwareSystem.getDescription());

        assertEquals(1, softwareSystem.getContainers().size());
        assertEquals("Web Application", softwareSystem.getContainers().iterator().next().getName());

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
    void test_extendWorkspaceFromDslFile_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        try {
            // this will fail, because the model import will be ignored
            parser.parse(new File("src/test/dsl/extend/extend-workspace-from-dsl-file.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Cannot import workspace from a file when running in restricted mode at line 1: workspace extends workspace.dsl {", e.getMessage());
        }
    }

    @Test
    void test_extendWorkspaceFromDslFiles() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/extend/4.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals(3, model.getPeople().size());
        assertEquals(1, views.getViews().size());
    }

    @Test
    void test_ref() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/ref.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/New deployment node/New infrastructure node"));
        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/US-East-1/New deployment node 1/New infrastructure node 1"));
        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/US-East-1/New deployment node 2/New infrastructure node 2"));
    }

    @Test
    void test_parallel() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/parallel.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
        DynamicView view = parser.getWorkspace().getViews().getDynamicViews().iterator().next();
        List<RelationshipView> relationships = new ArrayList<>(view.getRelationships());
        assertEquals(4, relationships.size());
        assertEquals("1", relationships.get(0).getOrder());
        assertEquals("2", relationships.get(1).getOrder());
        assertEquals("3", relationships.get(2).getOrder());
        assertEquals("3", relationships.get(3).getOrder());
    }

    @Test
    void test_groups() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/groups.dsl"));

        ContainerView containerView = parser.getWorkspace().getViews().getContainerViews().iterator().next();
        assertEquals(4, containerView.getElements().size());

        DeploymentView deploymentView = parser.getWorkspace().getViews().getDeploymentViews().iterator().next();
        assertEquals(6, deploymentView.getElements().size());
    }

    @Test
    void test_hierarchicalIdentifiers() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/hierarchical-identifiers.dsl"));

        Workspace workspace = parser.getWorkspace();
        assertEquals(0, workspace.getModel().getSoftwareSystemWithName("B").getRelationships().size());
    }

    @Test
    void test_hierarchicalIdentifiersAndDeploymentNodes() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/hierarchical-identifiers-and-deployment-nodes.dsl"));
    }

    @Test
    void test_pluginWithoutParameters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/plugin-without-parameters.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Java"));
    }

    @Test
    void test_pluginWithParameters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/plugin-with-parameters.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Java"));
    }

    @Test
    void test_script() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/script-external.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Groovy"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Kotlin"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Ruby"));
    }

    @Test
    void test_inlineScript() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/script-inline.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Groovy"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Kotlin"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Ruby"));
    }

    @Test
    void test_docs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/docs/workspace.dsl"));

        assertEquals(1, parser.getWorkspace().getDocumentation().getSections().size());
        assertEquals(1, parser.getWorkspace().getModel().getSoftwareSystemWithName("Software System").getDocumentation().getSections().size());
    }

    @Test
    void test_adrs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/adrs/workspace.dsl"));

        assertEquals(10, parser.getWorkspace().getDocumentation().getDecisions().size());
        assertEquals(10, parser.getWorkspace().getModel().getSoftwareSystemWithName("Software System").getDocumentation().getDecisions().size());
    }

    @Test
    void test_this() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/this.dsl"));
    }

    @Test
    void test_workspaceWithControlCharacters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/workspace-with-bom.dsl"));
    }

    @Test
    void test_excludeRelationships() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/exclude-relationships.dsl"));
    }

    @Test
    void test_urlNotPermittedInGroup() throws Exception {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(new File("src/test/dsl/group-url.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected tokens at line 6: url \"https://example.com\"", e.getMessage());
        }
    }

    @Test
    void test_multipleWorkspaceTokens_ThrowsAnException() throws Exception {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(new File("src/test/dsl/multiple-workspace-tokens.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple workspaces are not permitted in a DSL definition at line 9: workspace {", e.getMessage());
        }
    }

    @Test
    void test_multipleModelTokens_ThrowsAnException() throws Exception {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(new File("src/test/dsl/multiple-model-tokens.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple models are not permitted in a DSL definition at line 7: model {", e.getMessage());
        }
    }

    @Test
    void test_multipleViewTokens_ThrowsAnException() throws Exception {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(new File("src/test/dsl/multiple-view-tokens.dsl"));
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple view sets are not permitted in a DSL definition at line 13: views {", e.getMessage());
        }
    }

    @Test
    void test_dynamicViewWithExplicitRelationships() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/dsl/dynamic-view-with-explicit-relationships.dsl"));
    }

}