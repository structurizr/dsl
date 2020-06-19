# Language reference

The Structurizr DSL provides a way to define a software architecture model as text, using a domain specific language (DSL). The [Structurizr CLI](https://github.com/structurizr/cli) (command line interface) provides tooling to parse DSL workspace definitions, upload them to the Structurizr cloud service/on-premises installation, and export diagrams to other formats (e.g. PlantUML and WebSequenceDiagrams).

## General rules

- Line breaks are important, whitespace/indentation isn't.
- Keywords are case-insensitive (e.g. you can use ```softwareSystem``` or ```softwaresystem```).
- Double quote characters (```"```) are optional when a token contains no whitespace.
- Opening curly brace symbols (```{```) must be on the same line as the block they describe.
- Closing curly brace symbols (```}```) must be on a line on their own.

## Grammar

The following describes the language grammar.

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

The following keywords are supported.

- [workspace](#workspace)
	- [model](#model)
		- [enterprise](#enterprise)
		- [person](#person)
		- [softwareSystem](#softwareSystem)
			- [container](#container)
				- [component](#component)
	- [views](#views)


### workspace

```workspace``` is the top level language construct, and the wrapper for everything else defined in your software architecture model. A workspace can optionally be given a name and description.

```
workspace [name] [description] {
	...
}
```

For example:

```
 workspace "My workspace" "A workspace description." {
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

### views

Each workspace can also contain one or more views, defined with the ```views``` block.

```
views {
	...
}
```


