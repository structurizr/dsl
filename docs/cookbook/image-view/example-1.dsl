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