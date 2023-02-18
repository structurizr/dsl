workspace {

    views {
        properties {
            "plantuml.url" "http://localhost:7777"
            "mermaid.url" "http://localhost:8888"
            "kroki.url" "http://localhost:9999"
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