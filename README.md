![Structurizr](docs/images/structurizr-banner.png)

# Structurizr DSL

This GitHub repository contains an implementation of the Structurizr DSL - a way to create Structurizr software architecture models based upon the [C4 model](https://c4model.com) using a textual domain specific language (DSL). __This repository is supported by Structurizr Limited__, as a part of the Structurizr service.

## A quick example

As an example, the following text can be used to create a software architecture __model__ and an associated __view__ that describes a user using a software system.

```
workspace "Getting Started" "This is a model of my software system." {

    model {
        user = person "User" "A user of my software system."
        softwareSystem = softwareSystem "Software System" "My software system."

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem "SystemContext" "An example of a System Context diagram." {
            include *
            autoLayout
        }

        styles {
            element "Software System" {
                background #1168bd
                color #ffffff
            }
            element "Person" {
                shape person
                background #08427b
                color #ffffff
            }
        }
    }
    
}
```

The view can then be exported to be visualised using the [Structurizr cloud service/on-premises installation](https://structurizr.com), or other formats including PlantUML and WebSequenceDiagrams via the [Structurizr CLI](https://github.com/structurizr/cli).

## Table of contents

* [Getting started](docs/getting-started.md)
* [DSL grammar](grammar.txt)
* Examples
	* [Financial Risk System](examples/financial-risk-system.dsl)
	* [Big Bank plc](examples/big-bank-plc.dsl)
	* [Amazon Web Services](examples/amazon-web-services.dsl)