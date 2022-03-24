# Grammar

The following describes the language grammar, with angle brackets (`<...>`) used to show required properties, and square brackets (`[...]`) used to show optional properties.
Most statements are of the form: `keyword <required properties> [optional properties]`

## workspace

`workspace` is the top level language construct, and the wrapper for the [model](#model) and [views](#views). A workspace can optionally be given a name and description.

```
workspace [name] [description] {
    ...
}
```

A workspace can also extend another workspace, to add more elements, relationships, views, etc to it.

```
workspace extends <file|url> {
    ...
}
```

The base workspace can either be referenced using a local DSL/JSON file, or a remote (via a HTTPS URL) DSL/JSON file.
When extending a DSL-based workspace, all of the identifiers defined in that workspace are available to use in the extended workspace.

Permitted children:

- name <name>
- description <description>
- [properties](#properties)
- [!docs](#documentation)
- [!adrs](#architecture-decision-records-adrs)
- [!identifiers](#identifier-scope)
- [!impliedRelationships](#impliedrelationships)
- [model](#model)
- [views](#views)
- [configuration](#configuration)

## model

Each workspace must contain a `model` block, inside which elements and relationships are defined.

```
model {
    ...
}
```

Permitted children:

- [enterprise](#enterprise)
- [group](#group)
- [person](#person)
- [softwareSystem](#softwareSystem)
- [deploymentEnvironment](#deploymentEnvironment)
- [element](#element)
- [-> (relationship)](#relationship)

## enterprise

The `enterprise` keyword provides a way to define a named "enterprise" (e.g. an organisation) within the top-level model. Any people or software systems defined inside this block will be deemed to be "internal", while all others will be deemed to be "external". On System Landscape and System Context diagrams, an enterprise is represented as a dashed box. Only a single enterprise can be defined within a model.

```
enterprise [name] {
    ...
}
```

Permitted children:

- [group](#group)
- [person](#person)
- [softwareSystem](#softwareSystem)
- [-> (relationship)](#relationship)

## group

The `group` keyword provides a way to define a named grouping of elements, which will be rendered as a boundary around those elements.
See [groups.dsl](https://github.com/structurizr/dsl/tree/master/src/test/dsl/groups.dsl) for an example.

```
group <name> {
    ...
}
```

Groups can be defined as follows:

| Location | Permitted elements |
| ------------- | ------------- |
| Model | People and software systems |
| Software System | Containers |
| Container | Components |

Groups cannot be nested.

## person

The `person` keyword defines a person (e.g. a user, actor, role, or persona).

```
person <name> [description] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Person`

Permitted children:

- [description](#description)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [-> (relationship)](#relationship)

## softwareSystem

The `softwareSystem` keyword defines a software system.

```
softwareSystem <name> [description] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Software System`

Permitted children:

- [group](#group)
- [container](#container)
- [!docs](#documentation)
- [!adrs](#architecture-decision-records-adrs)
- [description](#description)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [-> (relationship)](#relationship)

## container

The `container` keyword defines a container, within a software system.

```
container <name> [description] [technology] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Container`

Permitted children:

- [group](#group)
- [component](#component)
- [description](#description)
- [technology](#technology)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [-> (relationship)](#relationship)

## component

The `component` keyword defines a component, within a container.

```
component <name> [description] [technology] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Component`

Permitted children:

- [description](#description)
- [technology](#technology)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [-> (relationship)](#relationship)

## deploymentEnvironment

The `deploymentEnvironment` keyword provides a way to define a deployment environment (e.g. development, testing, staging, live, etc).

```
deploymentEnvironment <name> {
    ...
}
```

Permitted children:

- [deploymentGroup](#deploymentGroup)
- [deploymentNode](#deploymentNode)
- [-> (relationship)](#relationship)

## deploymentGroup

The `deploymentGroup` keyword provides a way to define a named deployment group.

```
deploymentGroup <name>
```

When software system/container instances are added to a deployment environment,
all of the relationships between these elements are automatically replicated
between *all* instances. Deployment groups provide a way to restrict the scope
in which relationships are replicated. See
[deployment-groups.dsl](https://github.com/structurizr/dsl/tree/master/src/test/dsl/deployment-groups.dsl) for an example.

## deploymentNode

The `deploymentNode` keyword is used to define a deployment node.

```
deploymentNode <name> [description] [technology] [tags] [instances] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Deployment Node`

Permitted children:

- [deploymentNode](#deploymentNode) (deployment nodes can be nested)
- [infrastructureNode](#infrastructureNode)
- [softwareSystemInstance](#softwareSystemInstance)
- [containerInstance](#containerInstance)
- [-> (relationship)](#relationship)
- [description](#description)
- [technology](#technology)
- [instances](#instances)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)

## infrastructureNode

The `infrastructureNode` keyword defines an infrastructure node, which is typically something like a load balancer, firewall, DNS service, etc.

```
infrastructureNode <name> [description] [technology] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`
- `Infrastructure Node`

Permitted children:

- [-> (relationship)](#relationship)
- [description](#description)
- [technology](#technology)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)

## softwareSystemInstance

The `softwareSystemInstance` keyword defines an instance of the specified software system that is deployed on the parent deployment node.

```
softwareSystemInstance <identifier> [deploymentGroups] [tags] {
    ...
}
```

The `identifier` must represent a software system. `deploymentGroups` is a comma seperated list of identifiers representing deployment groups.

In addition to the software system's tags, the following tags are added by default:

- `Software System Instance`

Permitted children:

- [-> (relationship)](#relationship)
- [description](#description)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [healthCheck](#healthCheck)

## containerInstance

The `containerInstance` keyword defines an instance of the specified container that is deployed on the parent deployment node.

```
containerInstance <identifier> [deploymentGroups] [tags] {
    ...
}
```

The `identifier` must represent a container. `deploymentGroups` is a comma seperated list of identifiers representing deployment groups.

In addition to the container's tags, the following tags are added by default:

- `Container Instance`

Permitted children:

- [-> (relationship)](#relationship)
- [description](#description)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [healthCheck](#healthCheck)

## healthCheck

The `healthCheck` keyword defines a HTTP health check for the parent software system/container instance.

```
healthCheck <name> <url> [interval] [timeout]
```

The interval is a number of seconds (default 60s), and the timeout is a number of milliseconds (default 0ms).

## element

The `element` keyword defines a [custom element](https://structurizr.com/help/custom-elements) (this is only available on the Structurizr cloud service/on-premises installation/Lite).

```
element <name> [metadata] [description] [tags] {
    ...
}
```

The following tags are added by default:

- `Element`

Permitted children:

- [description](#description)
- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)
- [-> (relationship)](#relationship)

## relationship

`->` is used to define a uni-directional relationship between two elements.

There are two ways to define relationships. The first is explicitly, where you explicitly use a source identifier:

```
<identifier> -> <identifier> [description] [technology] [tags] {
    ...
}
```

For example:

```
user -> softwareSystem "Uses"
```

And the second is implicitly, where the relationship source is the element in scope:

```
-> <identifier> [description] [technology] [tags]
```

For example:

```
person user {
    -> softwareSystem "Uses"
}
```

This is equivalent to the following, where the special `this` identifier is used to refer to the element in scope:

```
person user {
    this -> softwareSystem "Uses"
}
```

The following tags are added to relationships by default:

- `Relationship`

The following types of relationships can be created using the DSL:

| Source  | Destination |
| ------------- | ------------- |
| Person | Person, Software System, Container, Component |
| Software System | Person, Software System, Container, Component |
| Container | Person, Software System, Container, Component |
| Component | Person, Software System, Container, Component |
| Deployment Node | Deployment Node |
| Infrastructure Node | Deployment Node, Infrastructure Node, Software System Instance, Container Instance |
| Software System Instance | Infrastructure Node |
| Container Instance | Infrastructure Node |

Permitted children:

- [tags](#tags)
- [url](#url)
- [properties](#properties)
- [perspectives](#perspectives)

## tags

`tags` is used to add tags to an element or relationship. Tags can be specified comma separated, or individually.

```
tags "Tag 1"
tags "Tag 1,Tag 2"
tags "Tag 1" "Tag 2"
```

## description

`description` is used to set the description on an element or view.

```
description "Description"
```

## technology

`technology` is used to set the technology on a container, component, deployment node, infrastructure node.

```
technology "Technology"
```

## instances

`instances` is used to set the number of instances of a deployment node.

```
instances "4"
```

## url

`url` is used to set a URL on an element or relationship.

```
url https://example.com
```

## properties

The `properties` block is used to define one or more name/value properties.

```
properties {
    <name> <value>
    ...
}
```

## perspectives

The `perspectives` block is used to define one or more name/description perspectives for an element or relationship.
See [Help - Perspectives](https://structurizr.com/help/perspectives) for how these are used.

```
perspectives {
    <name> <description>
    ...
}
```

## !ref

The `!ref` keyword provides a way to reference a previously defined element/relationship, and is designed to be used with the workspace `extends` or `!include` features. It can be used in a couple of ways.

The first usage scenario is to reference an existing element/relationship that has been defined via the DSL. This allows you to extend the element referenced by the given identifier.

```
!ref <identifier> {
  ...
}
```

Or, if you're extending a JSON-based workspace, you can reference an element by its "canonical name", and assign that to an identifier.

```
<identifier> = !ref <canonical name> {
  ...
}
```

See [ref.dsl](https://github.com/structurizr/dsl/tree/master/src/test/dsl/ref.dsl) for some usage examples.

__Please note that `!ref` is currently an experimental feature.__

## views

Each workspace can also contain one or more views, defined with the `views` block.

```
views {
    ...
}
```

The `views` block can contain the following:

- [systemLandscape](#systemLandscape-view)
- [systemContext](#systemContext-view)
- [container](#container-view)
- [component](#component-view)
- [filtered](#filtered-view)
- [dynamic](#dynamic-view)
- [deployment](#deployment-view)
- [custom](#custom-view)
- [styles](#styles)
- [theme](#theme)
- [themes](#themes)
- [branding](#branding)
- [terminology](#terminology)
- [properties](#properties)

## systemLandscape view

The `systemLandscape` keyword is used to define a [System Landscape view](https://c4model.com/#SystemLandscapeDiagram).

```
systemLandscape [key] [description] {
    ...
}
```

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## systemContext view

The `systemContext` keyword is used to define a [System Context view](https://c4model.com/#SystemContextDiagram) for the specified software system.

```
systemContext <software system identifier> [key] [description] {
    ...
}
```

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## container view

The `container` keyword is used to define a [Container view](https://c4model.com/#ContainerDiagram) for the specified software system.

```
container <software system identifier> [key] [description] {
    ...
}
```

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## component view

The `component` keyword is used to define a [Component view](https://c4model.com/#ComponentDiagram) for the specified container.

```
component <container identifier> [key] [description] {
    ...
}
```

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## filtered view

The `filtered` keyword is used to define a [Filtered view](https://structurizr.com/help/filtered-views) on top of the specified view.

```
filtered <baseKey> <include|exclude> <tags> [key] [description]
```

The `baseKey` specifies the key of the System Landscape, System Context, Container, or Component view on which this filtered view should be based. The mode (`include` or `exclude`) defines whether the view should include or exclude elements/relationships based upon the `tags` provided.

Please note that once a filtered view is defined for a given "base view", that base view will no longer show up in your diagram list when using the Structurizr renderer. This is by design. If you'd like to see the base view too, just create another filtered view for the same base view, which includes the `Element` and `Relationship` tags.

```
filtered <baseKey> include "Element,Relationship" [key] [description]
```

## dynamic view

The `dynamic` keyword defines a [Dynamic view](https://c4model.com/#DynamicDiagram) for the specified scope.

```
dynamic <*|software system identifier|container identifier> [key] [description] {
    ...
}
```

The first property defines the scope of the view, and therefore what can be added to the view, as follows:

- `*` scope: People and software systems.
- Software system scope: People, other software systems, and containers.
- Container scope: People, other software systems, other containers, and components.

Unlike the other diagram types, Dynamic views are created by specifying the relationships that should be added to the view, within the `dynamic` block, as follows:

```
<element identifier> -> <element identifier> [description] [technology]
<relationship identifier> [description]
```

With a dynamic view, you're showing _instances_ of relationships that are
defined in the static model. For example, imagine that you have two software
systems defined in the static model, with a single relationship between them
described as "Sends data to". A dynamic view allows you to override the
relationship description, to better describe the interaction in the context of
the behaviour you're diagramming. See [dynamic.dsl](https://github.com/structurizr/dsl/tree/master/src/test/dsl/dynamic.dsl)
for an example of this, and [Modelling multiple
  relationships](https://dev.to/simonbrown/modelling-multiple-relationships-51bf)
  for some tips on how to best model multiple relationships between two elements
    in order to avoid cluttering your static model. For convenience, if a
    relationship between the two elements does not exist in the static model,
    the DSL parser will automatically create it for you.

See [parallel.dsl](https://github.com/structurizr/dsl/tree/master/src/test/dsl/parallel.dsl) for an example of how to create
dynamic diagrams with parallel sequences.

Permitted children:

- [autoLayout](#autoLayout)
- [title](#title)
- [description](#description)
- [properties](#properties)

## deployment view

The `deployment` keyword defines a [Deployment view](https://c4model.com/#DeploymentDiagram) for the specified scope and deployment environment.

```
deployment <*|software system identifier> <environment> [key] [description] {
    ...
}
```

The first property defines the scope of the view, and the second property defines the deployment environment (which can be an identifier, or a name). The combination of these two properties determines what can be added to the view, as follows:

- `*` scope: All deployment nodes, infrastructure nodes, and container instances within the deployment environment.
- Software system scope: All deployment nodes and infrastructure nodes within the deployment environment. Container instances within the deployment environment that belong to the software system.

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## custom view

The `custom` keyword is used to define a [custom view](https://structurizr.com/help/custom-diagrams) (this is only available on the Structurizr cloud service/on-premises installation).

```
custom [key] [title] [description] {
    ...
}
```

Permitted children:

- [include](#include)
- [exclude](#exclude)
- [autoLayout](#autoLayout)
- [animation](#animation)
- [title](#title)
- [description](#description)
- [properties](#properties)

## include

The `include` keyword can be used to include elements or relationships.

### Including elements

To include elements in a view, use one or more `include` statements inside the block defining the view.

```
include <*|identifier|expression> [*|identifier|expression...]
```

Elements can either be specified using individual identifiers, the wildcard identifier (`*`), or a property expression. Please note that including elements will also include the relationships between those elements.

The wildcard identifier (`*`) operates differently depending upon the type of diagram, as follows:

- System Landscape view: Include all people and software systems.
- System Context view: Include the software system in scope; plus all people and software systems that are directly connected to the software system in scope.
- Container view: Include all containers within the software system in scope; plus all people and software systems that are directly connected to those containers.
- Component view: Include all components within the container in scope; plus all people, software systems and containers (belonging to the software system in scope) directly connected to them.
- Filtered view: (not applicable)
- Dynamic view: (not applicable)
- Deployment view: Include all deployment nodes, infrastructure nodes, and container instances defined within the deployment environment and (optional) software system in scope.

They provide a way to include elements based upon some basic conditional logic, as follows:

- `element.tag==<tag>,[tag]`: include elements that have all of the specified tags
- `element.tag!=<tag>,[tag]`: include elements that do not have all of the specified tags

### Including relationships

To include a relationship in a view, you can specify an individual relationship identifier, or an expression:

```
include <identifier|expression> [identifier|expression...]
```

Relationship expressions only operate on elements that exist in the view.

## exclude

The `exclude` keyword can be used to exclude elements or relationships.

### Excluding elements

To exclude specific elements, use one or more `exclude` statements inside the block defining the view.

```
exclude <identifier|expression> [identifier|expression...]
```

### Excluding relationships

To exclude a relationship in a view, you can specify an individual relationship identifier, or use a property expression:

```
exclude <identifier|expression> [identifier|expression...]
```

They provide a way to exclude relationships based upon some basic conditional logic, as follows:

- `relationship.tag==<tag>,[tag]`: exclude relationships that have all of the specified tags
- `relationship.tag!=<tag>,[tag]`: exclude relationships that do not have all of the specified tags

Alternatively, you can use the relationship expression syntax as follows (please note the double quotes surrounding the entire expression):

```
exclude "<*|identifier> -> <*|identifier>"
```

The combinations of parameters are:

- `* -> *`: all relationships between all elements
- `source -> *`: all relationships from `source` to any element
- `* -> destination`: all relationships from any element  to `destination`
- `source -> destination`: all relationships from `source` to `destination`

The relationship expression syntax only operates on elements that exist in the view.

## autoLayout

To enable automatic layout mode for the diagram, use the `autoLayout` statement inside the block defining the view.

```
autoLayout [tb|bt|lr|rl] [rankSeparation] [nodeSeparation]
```

The first property is the rank direction:

- `tb`: Top to bottom (default)
- `bt`: Bottom to top
- `lr`: Left to right
- `rl`: Right to left

The second property is the separation of ranks in pixels (default: `300`), while the third property is the separation of nodes in the same rank in pixels (default: `300`).

Please note that if your DSL workspace does not explicitly define any views, the DSL parser will automatically create a default set of views for you, with auto-layout enabled.
To change this behaviour, you can either (1) explicitly define your views or (2) use a script to disable automatic layout ([example](https://github.com/structurizr/dsl/tree/master/docs/cookbook/scripts#create-the-default-views-without-automatic-layout)).

## animation

The `animation` keyword defines the animation for the specified view.
Each animation step should be defined on a separate line, inside the block, specifying the elements that should be included in that step.

```
animation {
    <identifier> [identifier...]
    <identifier> [identifier...]
}
```

## title

Overrides the title of the view.

```
title <title>
```

## styles

`styles` is the wrapper for one or more element/relationship styles, which are used when rendering diagrams.

```
styles {
    ...
}
```

Permitted children:

- [element](#element-style)
- [relationship](#relationship-style)

## element style

The `element` keyword is used to define an element style. All nested properties (`shape`, `icon`, etc) are optional, see [Structurizr - Notation](https://structurizr.com/help/notation) for details about how tags and styles work.

```
element <tag> {
    shape <Box|RoundedBox|Circle|Ellipse|Hexagon|Cylinder|Pipe|Person|Robot|Folder|WebBrowser|MobileDevicePortrait|MobileDeviceLandscape|Component>
    icon <file|url>
    width <integer>
    height <integer>
    background <#rrggbb>
    color <#rrggbb>
    colour <#rrggbb>
    stroke <#rrggbb>
    strokeWidth <integer: 1-10>
    fontSize <integer>
    border <solid|dashed|dotted>
    opacity <integer: 0-100>
    metadata <true|false>
    description <true|false>
    properties {
        name value
    }
}
```

Please note that element styles are designed to work with the Structurizr cloud service/on-premises installation/Lite, and may not be fully supported by the PlantUML, Mermaid, etc export formats.

Important note: see [Help - Icons](https://structurizr.com/help/icons) if you are specifying an element style icon via a URL.


## relationship style

The `relationship` keyword is used to define a relationship style. All nested properties (`thickness`, `color`, etc) are optional, see [Structurizr - Notation](https://structurizr.com/help/notation)  for details about how tags and styles work.

```
relationship <tag> {
    thickness <integer>
    color #777777
    colour #777777
    style <solid|dashed|dotted>
    routing <Direct|Orthogonal|Curved>
    fontSize <integer>
    width <integer>
    position <integer: 0-100>
    opacity <integer: 0-100>
    properties {
        name value
    }
}
```

Please note that relationship styles are designed to work with the Structurizr cloud service/on-premises installation/Lite, and may not be fully supported by the PlantUML, Mermaid, etc export formats.

## theme

The `theme` keyword can be used to specify a theme that should be used when rendering diagrams. See [Structurizr - Themes](https://structurizr.com/help/themes) for more details.

```
theme <default|url>
```

`default` can be used as a theme URL, to include [the default Structurizr theme](https://structurizr.com/help/theme?url=https://static.structurizr.com/themes/default/theme.json).

## themes

The `themes` keyword can be used to specify one or more themes that should be used when rendering diagrams. See [Structurizr - Themes](https://structurizr.com/help/themes) for more details.

```
themes <url> [url] ... [url]
```

`default` can be used as a theme URL, to include [the default Structurizr theme](https://structurizr.com/help/theme?url=https://static.structurizr.com/themes/default/theme.json).

## branding

The `branding` keyword allows you to define some custom branding that should be used when rendering diagrams and documentation. See [Structurizr - Branding](https://structurizr.com/help/branding) for more details.

```
branding {
    logo <file|url>
    font <name> [url]
}
```

Important note: see [Help - Icons](https://structurizr.com/help/icons) if you are specifying a branding logo via a URL.

## terminology

The `terminology` keyword allows you to override the terminology used when rendering diagrams (this may not be supported in all rendering tools). See [Structurizr - Terminology](https://structurizr.com/help/terminology) for more details.

```
terminology {
    enterprise <term>
    person <term>
    softwareSystem <term>
    container <term>
    component <term>
    deploymentNode <term>
    infrastructureNode <term>
    relationship <term>
}
```

## configuration

Finally, there are some configuration options that can be specified inside the `configuration` block.

```
configuration {
    ...
}
```

Permitted children:

- [users](#users)
- [properties](#properties)

## users

The `users` block can be used to specify the users who should have read-only or read-write access to a workspace. Each username (e.g. e-mail address) and role pair should be specified on their own line. Valid roles are `read` (read-only) and `write` (read-write).

```
users {
    <username> <read|write>
}
```

## Documentation

The `!docs` keyword can be used to attach Markdown/AsciiDoc documentation to the parent context (either the workspace, or a software system).

```
!docs <path> <fully qualified class name>
```

The path must be a relative path, located within the same directory as the parent file, or a subdirectory of it. For example:

```
!docs subdirectory
```

By default, the [com.structurizr.documentation.importer.DefaultDocumentationImporter](https://github.com/structurizr/documentation/blob/main/src/main/java/com/structurizr/documentation/importer/DefaultDocumentationImporter.java) class will be used to import documentation as follows:

- All Markdown and AsciiDoc files in the given directory will be imported, alphabetically according to the filename.
- Each file must represent a separate documentation section, and the second level heading (`## Section Title` in Markdown and `== Section Title` in AsciiDoc) will be used as the section name.
- All images in the given directory (and sub-directories) are also imported into the workspace.

The above behaviour can be customised by specifying the fully qualified class name of your own implementation of [DocumentationImporter](https://github.com/structurizr/documentation/blob/main/src/main/java/com/structurizr/documentation/importer/DocumentationImporter.java), which needs to be on the DSL classpath or installed as a JAR file in the `plugins` directory next to your DSL file.

## Architecture decision records (ADRs)

The `!adrs` keyword can be used to attach Markdown/AsciiDoc ADRs to the parent context (either the workspace, or a software system).

```
!adrs <path> <fully qualified class name>
```

The path must be a relative path, located within the same directory as the parent file, or a subdirectory of it. For example:

```
!adrs subdirectory
```

By default, the [com.structurizr.documentation.importer.AdrToolsDecisionImporter](https://github.com/structurizr/documentation/blob/main/src/main/java/com/structurizr/documentation/importer/AdrToolsDecisionImporter.java) class will be used to import ADRs as follows:

- All Markdown files in this directory will be imported, alphabetically according to the filename.
- The files must have been created by [adr-tools](https://github.com/npryce/adr-tools), or at least follow the same format.
- All images in the given directory (and sub-directories) are also imported into the workspace.

The above behaviour can be customised by specifying the fully qualified class name of your own implementation of [DocumentationImporter](https://github.com/structurizr/documentation/blob/main/src/main/java/com/structurizr/documentation/importer/DocumentationImporter.java), which needs to be on the DSL classpath or installed as a JAR file in the `plugins` directory next to your DSL file.
