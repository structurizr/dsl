# DSL and code

It's possible to use both the DSL and a code-based library together - perhaps you want to define a basic model via the DSL, and use automatic extraction to add components to the model. To do this, define your workspace using the DSL as normal, for example:

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

## Java

Since the DSL parser is written in Java, you can use it directly by adding a dependency on the `structurizr-dsl` library, which is available on [Maven Central](https://search.maven.org/artifact/com.structurizr/structurizr-dsl):

- groupId: `com.structurizr`
- artifactId: `structurizr-dsl`

You can then write a Java program that parses your DSL definition, and extends the workspace further; for example:

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

Please note that the Structurizr for Java library is designed to be *append only*, so it's not possible to remove/modify elements/relationships that already exist in the model.

## Other languages

Ports of the "Structurizr for Java" library are available for a number of other programming languages - see [https://structurizr.org/#authoring](https://structurizr.org/#authoring) for links. Although you can't use the DSL parser directly, you can achieve the same effect with the following steps:

### 1. Convert your DSL workspace to the JSON format

This can be done using the [Structurizr CLI export command](https://github.com/structurizr/cli/blob/master/docs/export.md):

```
structurizr export -workspace workspace.dsl -format json
```

### 2. Load the JSON file, and add to the workspace

A Structurizr for .NET version of the above example is as follows:

```
FileInfo fileInfo = new FileInfo("workspace.json");
Workspace workspace = WorkspaceUtils.LoadWorkspaceFromJson(fileInfo);
Container webApplication = workspace.Model.GetSoftwareSystemWithName("Software System").GetContainerWithName("Web Application");

// add components manually or via automatic extraction
...

// add a component view
ComponentView componentView = workspace.Views.CreateComponentView(webApplication, "Components", "Description");
componentView.AddDefaultElements();
componentView.EnableAutomaticLayout();
```