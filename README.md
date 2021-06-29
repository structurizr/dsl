![Structurizr](docs/images/structurizr-banner.png)

# Structurizr DSL

This GitHub repository contains an implementation of the Structurizr DSL - a way to create Structurizr software architecture models based upon the [C4 model](https://c4model.com) using a textual domain specific language (DSL). The Structurizr DSL has appeared on the [ThoughtWorks Tech Radar - Techniques - Diagrams as code](https://www.thoughtworks.com/radar/techniques?blipid=202010027).

##  Getting started

* [Getting started with Structurizr Lite](https://dev.to/simonbrown/getting-started-with-structurizr-lite-27d0) (using the DSL)
* [Getting started with the Structurizr CLI and DSL](https://github.com/structurizr/cli/blob/master/docs/getting-started.md)
* [Structurizr DSL demo](https://structurizr.com/dsl)

##  Reference

* [Language reference](docs/language-reference.md)
* [Changelog](docs/changelog.md)
* [Structurizr DSL cookbook](docs/cookbook) (this is a work in progress)

## A quick example

As an example, the following text can be used to create a software architecture __model__ and an associated __view__ that describes a user using a software system.

```
workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System"

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem {
            include *
            autolayout
        }

        theme default
    }
    
}
```

([view this example](https://structurizr.com/dsl?example=getting-started))

## Multiple output formats

The DSL can be rendered using a number of tools:

- [Structurizr Lite](https://structurizr.com/help/lite) (free)
- [Structurizr cloud service/on-premises installation](https://structurizr.com) (free and paid), PlantUML, Mermaid, DOT (Graphviz), WebSequenceDiagrams, and Ilograph via the [Structurizr CLI](https://github.com/structurizr/cli)

## Convention over configuration, useful defaults

The DSL is designed to be as compact as possible. In conjunction with the Structurizr CLI, the following DSL fragment will automatically create the implied relationship between the ```user``` and ```softwareSystem``` elements, create three views with auto-layout enabled (1 x System Landscape, 1 x System Context, 1 x Container), and add some default element styles.

```
workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" {
            webapp = container "Web Application"
            database = container "Database"
         }

        user -> webapp "Uses"
        webapp -> database "Reads from and writes to"
    }
    
    views {
    	theme default
    }

}
```

## Tooling support

Here are some tools designed to support the Structurizr DSL.

* [Structurizr Lite](https://structurizr.com/help/lite)
* [Structurizr CLI](https://github.com/structurizr/cli)
* [VS Code extension - syntax highlighting and diagram previews](https://marketplace.visualstudio.com/items?itemName=systemticks.c4-dsl-extension) (please note that there are some known issues with the syntax highlighting - see [Divergences](https://gitlab.com/systemticks/c4-grammar/-/tree/master/extension#divergences) for more details, and [Examples](https://gitlab.com/systemticks/c4-grammar/-/tree/master/workspace) for examples that work correctly with the extension)
* [VS Code extension - syntax highlighting](https://marketplace.visualstudio.com/items?itemName=ciarant.vscode-structurizr)

## Examples

* [Getting started](https://structurizr.com/dsl?example=getting-started)
* [Big Bank plc](https://structurizr.com/dsl?example=big-bank-plc)
* [Amazon Web Services](https://structurizr.com/dsl?example=amazon-web-services)