# Changelog

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