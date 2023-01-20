# Changelog

## 1.24.0 (unreleased)

- More variables are exposed to scripts, based upon where the script is defined (`element`, `relationship`, `view`). 

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