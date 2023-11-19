# Changelog

## 1.34.0 (19th November 2023)

- Fixes https://github.com/structurizr/dsl/issues/364 (.DS_Store file causes exception during !include <directory> on Windows).
- Fixes https://github.com/structurizr/dsl/issues/359 (Add url for relationship in dynamic view).
- Adds a `getDslParser()` method to the `StructurizrDslPluginContext` class (https://github.com/structurizr/dsl/issues/361).
- Adds the ability to specify the workspace scope via a `scope` keyword inside the workspace `configuration`.
- Adds support for specifying perspective values.
- Updates structurizr/java to [v1.28.0](https://github.com/structurizr/java/releases/tag/v1.28.0).
- Updates structurizr/import to [v1.7.0](https://github.com/structurizr/import/releases/tag/v1.7.0).

## 1.33.0 (27th October 2023)

- DSL identifiers (if present) will now be loaded when extending a JSON workspace (see https://github.com/structurizr/dsl/discussions/328).
- Adds a `context` variable to inline/external scripts (see https://github.com/structurizr/dsl/issues/332).
- Fixes https://github.com/structurizr/dsl/issues/324 (Groups with no curly braces breaks diagrams).
- Adds a way to set the character encoding used by the DSL parser (see https://github.com/structurizr/dsl/issues/338).
- Fixes https://github.com/structurizr/dsl/issues/336 (Dynamic View does not allow !script tag).
- `!extend` can now be used instead of `!ref`.
- Updates structurizr/java to [v1.27.0](https://github.com/structurizr/java/releases/tag/v1.27.0).
- Updates structurizr/import to [v1.6.0](https://github.com/structurizr/import/releases/tag/v1.6.0).

## 1.32.0 (28th July 2023)

- Adds the ability to specify the workspace `visibility` (private/public) via the workspace configuration.
- Updates structurizr/java to [v1.26.1](https://github.com/structurizr/java/releases/tag/v1.26.1).

## 1.31.1 (26th July 2023)

- Fixes https://github.com/structurizr/dsl/issues/308 (Hidden (e.g. .DS_Store) file causes exception during !include <directory>).
- Updates structurizr/java to [v1.25.1](https://github.com/structurizr/java/releases/tag/v1.25.1).

## 1.31.0 (22nd July 2023)

- Adds support for passing parameters to external scripts.
- Updates structurizr/java to [v1.25.0](https://github.com/structurizr/java/releases/tag/v1.25.0).
- Updates structurizr/import to [v1.5.0](https://github.com/structurizr/import/releases/tag/v1.5.0).

## 1.30.4 (17th July 2023)

- Adds support for more easily including/excluding implied relationships in view definitions (https://github.com/structurizr/dsl/issues/303).

## 1.30.3 (4th July 2023)

- Fixes https://github.com/structurizr/dsl/issues/289 (Cannot invoke "Object.equals(Object)" because "r" is null).

## 1.30.2 (20th June 2023)

- Fixes https://github.com/structurizr/dsl/issues/242 (deploymentViews hierarchical identifiers issue).
- Makes `StructurizrDslPluginContext` constructor public to allow for unit testing.
 
## 1.30.1 (5th April 2023)

- Fixes https://github.com/structurizr/dsl/issues/241 (Allow styles defined in an extending workspace to override those in the base workspace).
- Upgrades structurizr/java to [v1.24.1](https://github.com/structurizr/java/releases/tag/v1.24.1).

## 1.30.0 (31st March 2023)

- Allows `deploymentEnvironment` to be used without starting a new context (i.e. without `{` and `}`) (see https://github.com/structurizr/cli/discussions/112).
- Adds support for splitting lines in the DSL source with a backslash character (https://github.com/structurizr/dsl/issues/137).
- Fixes https://github.com/structurizr/dsl/issues/114 (Parallel sequence behavior in dynamic views).
- Fixes https://github.com/structurizr/dsl/issues/239 (File context for included files varies based upon how they are included).
- Adds support for grouping deployment nodes, infrastructure nodes, software system instances, and container instances.
- Upgrades structurizr/java to [v1.24.0](https://github.com/structurizr/java/releases/tag/v1.24.0).

## 1.29.1 (17th March 2023)

- Upgrades structurizr/java to [v1.23.1](https://github.com/structurizr/java/releases/tag/v1.23.1).
- Upgrades structurizr/import to [v1.4.1](https://github.com/structurizr/import/releases/tag/v1.4.1).

## 1.29.0 (12th March 2023)

- Adds support for nested groups.
- Upgrades structurizr/java to [v1.23.0](https://github.com/structurizr/java/releases/tag/v1.23.0).

## 1.28.3 (11th March 2023)

- Upgrades structurizr/java to [v1.22.3](https://github.com/structurizr/java/releases/tag/v1.22.3).
- Silently ignore `!plugin` and `!script` when running in restricted mode, to match how `!docs` and `!adrs` work.

## 1.28.0 (5th March 2023)

- Upgrades structurizr/import to [v1.4.0](https://github.com/structurizr/import/releases/tag/v1.4.0).
- Adds support for component level documentation/decisions.

## 1.27.1 (26th February 2023)

- Upgrades structurizr/import to [v1.3.1](https://github.com/structurizr/import/releases/tag/v1.3.1).

## 1.27.0 (26th February 2023)

- Attempts to set the content type for image views when not specified.
- Upgrades structurizr/java to [v1.21.0](https://github.com/structurizr/java/releases/tag/v1.21.0).
- Upgrades structurizr/import to [v1.3.0](https://github.com/structurizr/import/releases/tag/v1.3.0).

## 1.26.2 (21st February 2023)

- Zero pads automatically generated view keys for better sorting. 

## 1.26.1 (18th February 2023)

- Sets titles for image view content specified via URLs.
- Adds a `default` keyword, for setting the default view.

## 1.26.0 (17th February 2023)

- Adds the element/relationship identifiers into the model (as an element/relationship property named `"structurizr.dsl.identifier"`).
- Adds support for image views.

## 1.25.0 (3rd February 2023)

- Fixes some clashing issues with automatic key generation for views.

## 1.24.0 (28th January 2023)

- More variables are exposed to scripts, based upon where the script is defined (`element`, `relationship`, `view`).
- Improved the "unexpected tokens" error message to include a list of expected tokens.

## 1.23.0 (15th January 2023)

- Adds support for using (CSS/HTML) named colors instead of hex color codes.

## 1.22.0 (5th January 2023)

- Fixes #194 (Disabling the online DSL editor).
- Adds support for deployment node instance ranges (e.g. 0..1, 1..3, 5..10, 0..N, 0..*, 1..N, 1..*, etc).

## 1.21.1 (23rd December 2022)

- Updated dependencies.

## 1.21.0 (21st December 2022)

- Fixes #153 (Error when deploymentNode identifier is the same as a softwareSystem or container identifier)
- Adds support for custom elements on dynamic views.
- The DSL parser will now throw an error if the enterprise is set more than once (#159).
- Adds the ability to set name/value properties on element and relationship styles.
- Adds support for setting element style stroke widths.
- Adds name-value properties to views.
- Adds support for using `theme` and `themes` inside the `styles` block.
- __Breaking change__: Dynamic views will no longer create relationships that don't exist in the model.

## 1.20.0 (15th August 2022)

- Fixes #130 (Incorrect displaying of multiple urls within a container).
- Added more validation for supported icon types (PNG and JPG, not SVG).
- Adds support for `instances` as child of deploymentNode (#133).
- Fixes #142 (Invalid object scoping strategy).
- Adds support for explicitly specifying the relationships that should be added to dynamic views.
- Adds support for setting workspace properties.

## 1.19.1 (30th March 2022)

- `title` now works for custom views.
- Adds `description` to set view descriptions.

## 1.19.0 (23rd March 2022)

- Uses the new documentation/ADR importers, and makes it possible to use a custom implementation via `!docs` and `!adrs`.

## 1.18.0 (20th February 2022)

- Removes backwards compatibility for unquoted expressions (e.g. `exclude src -> dest` - use `exclude "src -> dest"` instead).
- Adds the ability to include/exclude relationships by the tag of source of destination elements (e.g. `exclude "* -> element.tag==Tag"`).
- Adds support for directory based includes with `!include`.
- Adds support for element expressions to be used in the afferent/efferent coupling expression (e.g. `exclude "->element.tag==Tag"`)
- Adds support for element expressions of the form `element.parent==<identifier>`.
- Fixes #113 (Excluding relationships with tags in a Deployment Diagram).

## 1.17.0 (4th January 2022)

- Adds support for extending deployment environments via the `!ref` keyword (issue #92).
- Adds support for extending relationships via the `!ref` keyword (issue #93).
- Fixes #94 (tabs cause parsing errors).
- Adds support for different relationship line styles (solid, dashed, dotted).
- Adds support for name/value properties on the view set.

## 1.16.0 (26th November 2021)

- Adds the implied relationships functionality for custom elements.
- The "add default elements" feature (`include *`) will now also add any connected custom elements.
- Adds better support for custom elements when using element expressions.
- Adds a `description` keyword for setting the description on elements.
- Adds a `technology` keyword for setting the technology on containers, components, deployment nodes, and infrastructure nodes.

## 1.15.0 (2nd October 2021)

- Adds support for specifying element style icons and the branding logo as a HTTPS/HTTP URL.
- Adds support for relationships from deployment nodes to infrastructure nodes.
- Fixes an issue where `this` didn't work when defining relationships inside deployment/infrastructure nodes.
- Removes the restriction that `include *` must be on a line of its own inside view definitions.

## 1.14.0 (19th September 2021)

- __Breaking change__: Adds support for software system/container instances in multiple deployment groups.
- Fixes an issue where internal software systems/people are not included by the DSL formatter when the enterprise is not set.
- The DSL formatter now removes empty tokens at the end of lines.
- Adds support for recognising escaped newlines (\n) in tokens.

## 1.13.0 (3rd September 2021)

- __Breaking change__: `impliedRelationships` is now `!impliedRelationships`.
- Adds support for "include relationship==*" (#68).
- Fixes #69 (hierarchical identifiers not working at the top level inside a deployment environment).
- Adds experimental support for `!ref`, `!plugin`, and `!script`.

## 1.12.0 (30th June 2021)

- Adds an `!identifiers` keyword to specify whether element identifiers should be `flat` (default) or `hierarchical`.
- Adds support for a `this` identifier when defining relationships inside element definitions.
- Fixes links between ADRs.

## 1.11.0 (7th June 2021)

- __Breaking change__: Default styles are no longer added; use the `default` theme to add some default styles instead.
- __Breaking change__: Relationship expressions (e.g. * -> *) now need to be surrounded in double quotes.
- Support parallel activities in dynamic view (issue #53).
- Adds a `tags` keyword for adding tags to elements/relationships.
- Adds a `theme` keyword for adding a single theme.
- Adds support to `!include` from a HTTPS URL.
- Adds support for referencing groups by identifier.
- Adds support for extending a workspace.

## 1.10.0 (27th April 2021)

- First version released onto Maven Central.