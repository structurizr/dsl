workspace {

    views {
        properties {
            "plantuml.url" "https://plantuml.com/plantuml"
            "mermaid.url" "https://mermaid.ink"
            "kroki.url" "https://kroki.io"
        }

        image * "plantuml" {
            plantuml diagram.puml
        }

        image * "mermaid" {
            mermaid diagram.mmd
        }

        image * "kroki" {
            kroki graphviz diagram.dot
        }

        image * "image" {
            image logo.png
        }
    }

}