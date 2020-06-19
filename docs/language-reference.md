# Language reference

The Structurizr DSL provides a way to define a software architecture model as text, using a domain specific language (DSL). The [Structurizr CLI](https://github.com/structurizr/cli) (command line interface) provides tooling to parse DSL workspace definitions, upload them to the Structurizr cloud service/on-premises installation, and export diagrams to other formats (e.g. PlantUML and WebSequenceDiagrams).

## Table of contents

- [General rules](#general-rules)
- [Identifiers](#identifiers)
- [Grammar](#grammar)
- [Keywords](#keywords)
	- [workspace](#workspace)
		- [model](#model)
			- [enterprise](#enterprise)
			- [person](#person)
			- [softwareSystem](#softwareSystem)
				- [container](#container)
					- [component](#component)
		- [views](#views)

## General rules

- Line breaks are important, whitespace/indentation isn't.
- Keywords are case-insensitive (e.g. you can use ```softwareSystem``` or ```softwaresystem```).
- Double quote characters (```"```) are optional when a property contains no whitespace.
- Opening curly brace symbols (```{```) must be on the same line (i.e. the last token of the statement, not on a line on their own).
- Closing curly brace symbols (```}```) must be on a line on their own.
- Use ```""``` as a placeholder for an earlier optional property.

## Identifiers

By default, all elements and relationships are anonymous, in that they can't be referenced from within the DSL. For example, the following statements will create a person and a software system, but neither can be referenced within the DSL. 

```
person "User" "A user of my software system."
softwareSystem "Software System" "My software system"
```

To create a relationship between the two elements, we need to be able to reference them. We can do this by definining an identifier, in the same way that you'd define a variable in many programming languages.

```
p = person "User" "A user of my software system."
ss = softwareSystem "Software System" "My software system"
```

Now we can use these identifiers when creating relationships, specifying which elements should be included/excluded from views, etc.

```
p -> ss "Uses"
```

Identifiers are only needed where you plan to reference the element/relationship.

## Grammar

The following describes the language grammar, with angle brackets (```<...>```) used to show required properties, and square brackets (```[...]```) used to show optional properties.

Most statements are of the form: ```keyword <required properties> [optional properties]```

```
workspace [name] [description] {

    /**
        multi-line comment
    */

    # single line comment
    // single line comment

    !include <file>

    model {

        enterprise <name> {
            [<identifier> = ]person <name> [description] [tags]
            [<identifier> = ]softwareSystem = softwareSystem <name> [description] [tags] {
                [<identifier> = ]container <name> [description] [technology] [tags] {
                    [<identifier> = ]component <name> [description] [technology] [tags]
                }
            }
        }

        <identifier> -> <identifier> [description] [technology] [tags]

        deploymentEnvironment <name> {
            [<identifier> = ]deploymentNode <name> [description] [technology] [tags] {
                [<identifier> = ]deploymentNode <name> [description] [technology] [tags] {
                    [<identifier> = ]infrastructureNode <name> [description] [technology] [tags]
                        [<identifier> = ]containerInstance <identifier>
                }
            }
        }

    }
         
    views {

        systemLandscape <key> [description] {
            elements <*|identifier(s)>
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
            animationStep <identifier> [identifier...]
        }

        systemContext <software system identifier> <key> [description] {
            include <*|identifier> [identifier...]
            exclude <identifier> [identifier...]
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
            animationStep <identifier> [identifier...]
        }

        container <software system identifier> <key> [description] {
            include <*|identifier> [identifier...]
            exclude <identifier> [identifier...]
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
            animationStep <identifier> [identifier...]
        }

        component <container identifier> <key> [description] {
            include <*|identifier> [identifier...]
            exclude <identifier> [identifier...]
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
            animationStep <identifier> [identifier...]
        }

        filtered <baseKey> <include|exclude> <tags> <key> [description]

        dynamic <*|software system identifier|container identifier> <key> [description] {
            <identifier> -> <identifier> [description]
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
        }

        deployment * <environment name> <key> [description] {
            include <*|identifier> [identifier...]
            autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
            animationStep <identifier> [identifier...]
        }

        styles {
            element <tag> {
                shape <Box|RoundedBox|Circle|Ellipse|Hexagon|Cylinder|Pipe|Person|Robot|Folder|WebBrowser|MobileDevicePortrait|MobileDeviceLandscape|Component>
                icon <file>
                width <integer>
                height <integer>
                background <#rrggbb>
                color <#rrggbb>
                colour <#rrggbb>
                stroke <#rrggbb>
                fontSize <integer>
                border <solid|dashed|dotted>
                opacity <integer: 0-100>
                metadata <true|false>
                description <true|false>
            }

            relationship <tag> {
                thickness <integer>
                color #777777
                colour #777777
                dashed <true|false>
                routing <Direct|Orthogonal|Curved>
                fontSize <integer>
                width <integer>
                position <integer: 0-100>
                opacity <integer: 0-100>
            }
        }

        themes <themeUrl> [themeUrl] ... [themeUrl]

        branding {
            logo <file>
            font <name> [url]
        }

    }

}
```

## Keywords

A reference for all keywords is as follows.

### workspace

```workspace``` is the top level language construct, and the wrapper for everything else defined in your software architecture model. A workspace can optionally be given a name and description.

```
workspace [name] [description] {
	...
}
```

### model

Each workspace must contain a ```model``` block, inside which elements and relationships are defined.

```
model {
	...
}
```

The model can contain the following elements:

- [enterprise](#enterprise)
- [person](#person)
- [softwareSystem](#softwareSystem)

## enterprise

The ```enterprise``` keyword provides a way to define a named "enterprise" (e.g. an organisation). Any people or software systems defined inside this block will be deemed to be "internal", while all others will be deemed to be "external". On System Landscape and System Context diagrams, an enterprise is represented as a dashed box. Only a single enterprise can be defined within a model.

```
enterprise <name> {
	...
}
```

## person

The ```person``` keyword defines a person (e.g. a user, actor, role, or persona).

```
person <name> [description] [tags]
```

## softwareSystem

The ```softwareSystem``` keyword defines a software system.

```
softwareSystem <name> [description] [tags]
```

The ```softwareSystem``` keyword can either used on its own, or with braces if you would like to define child containers:

```
softwareSystem <name> [description] [tags] {
	...
}
```

## container

The ```container``` keyword defines a container, within a software system.

```
container <name> [description] [technology] [tags]
```

The ```container``` keyword can either used on its own, or with braces if you would like to define child components:

```
container <name> [description] [technology] [tags] {
	...
}
```

## component

The ```component``` keyword defines a component, within a container.

```
component <name> [description] [technology] [tags]
```

### views

Each workspace can also contain one or more views, defined with the ```views``` block.

```
views {
	...
}
```


