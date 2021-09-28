# DSL and code hybrid usage

It's possible to use both the DSL and the Structurizr for Java library together - perhaps you want to define a basic model via the DSL, and use automatic extraction to add components to the model. To do this, define your workspace using the DSL as normal, for example:

```
workspace {

    model {
        s = softwareSystem "Software System" {
            webapp = container "Web Application"
            database = container "Database" {
                webapp -> this "Reads from and writes to"
            }
        }
    }

    views {
        container s {
            include *
            autoLayout lr
        }
    }
    
}
```

You can then write a Java program that parses this definition, and extends the workspace further; for example:

```
StructurizrDslParser parser = new StructurizrDslParser();
parser.parse(new File("workspace.dsl"));

Workspace workspace = parser.getWorkspace();
Container webApplication = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Web Application");

// add components manually or via automatic extraction
...

// add a component view
ComponentView componentView = workspace.getViews().createComponentView(webApplication, "Components", "Description");
componentView.addDefaultElements();
componentView.enableAutomaticLayout();
```

You'll need to include a dependency on the `structurizr-dsl` library, which is available on [Maven Central](https://search.maven.org/artifact/com.structurizr/structurizr-dsl). Please note that the Structurizr for Java library is designed to be *append only*, so it's not possible to remove/modify elements/relationships that already exist in the model. 