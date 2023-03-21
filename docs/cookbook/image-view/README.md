# Image view

An [image view](https://structurizr.com/help/image-views) represents an image that has been rendered outside of Structurizr, for inclusion in a Structurizr workspace.
Image views can be created from an arbitrary PNG/SVG file, or PlantUML/Mermaid/Kroki diagram.
An example use case for an image view is to provide a "level 4" zoom-in for a component on a component diagram.

```
workspace {

    model {
        softwareSystem "Software System" {
            container = container "Container" {
                component1 = component "Component 1"
            }
        }
    }

    views {
        properties {
            "mermaid.url" "https://mermaid.ink"
            "mermaid.format" "svg"
        }

        component container {
            include *
            autoLayout lr
        }

        image component1 {
            mermaid https://raw.githubusercontent.com/structurizr/dsl/master/docs/cookbook/image-view/component1.mmd
            title "Class diagram for Component1"
        }
    }
    
}
```

This DSL defines an image view for the "Component 1" component, based upon a Mermaid class diagram.

[![](example-1.png)](http://structurizr.com/dsl?src=https://raw.githubusercontent.com/structurizr/dsl/master/docs/cookbook/image-view/example-1.dsl)

## Links

- [Structurizr - Help - Image views](https://structurizr.com/help/image-views)
- [DSL language reference - image view](https://github.com/structurizr/dsl/blob/master/docs/language-reference.md#image-view)