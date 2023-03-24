# Structurizr DSL cookbook

Creating software architecture diagrams from a textual definition ("diagrams as code") is becoming more popular,
but if you have a collection of related diagrams, it's easy to introduce inconsistencies if you don't keep the many
diagram source files in sync.

This cookbook is a tutorial guide to the Structurizr DSL, an open source tool for creating diagrams as code from
a single consistent model. This cookbook assumes that you're using the diagram renderer provided by the
[Structurizr cloud service](https://structurizr.com/help/cloud-service),
the [Structurizr on-premises installation](https://structurizr.com/help/on-premises),
or [Structurizr Lite](https://structurizr.com/help/lite).
Please note that some features (e.g. perspectives, element style shapes/icons, etc) may not be supported if you're
using one of the PlantUML/Mermaid/D2/DOT/etc export formats provided by the
[Structurizr CLI](https://github.com/structurizr/cli) and the [structurizr-export library](https://github.com/structurizr/export).

## Table of contents

- [Introduction](introduction)
- [Workspace](workspace)
- [Workspace extension](workspace-extension)
- [Implied relationships](implied-relationships)
- [System Context view](system-context-view)
- [Container view](container-view)
- [Container view (spanning multiple software systems)](container-view-for-multiple-software-systems)
- [Component view](component-view)
- [Shared components](shared-components)
- [Image view](image-view)
- [Filtered view](filtered-view)
- [Dynamic view](dynamic-view)
- [Dynamic view with parallel sequences](dynamic-view-parallel)
- [Deployment view](deployment-view)
- [Amazon Web Services](amazon-web-services)
- [Deployment groups](deployment-groups)
- [Element styles](element-styles)
- [Relationship styles](relationship-styles)
- [Themes](themes)
- [Groups](groups)
- [Perspectives](perspectives)
- [Scripts](scripts)
- [DSL and code (hybrid usage pattern)](dsl-and-code)