workspace {

    !docs docs com.structurizr.example.ExampleDocumentationImporter

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" {
            !docs docs

            container "Container" {
                !docs docs

                component "Component" {
                    !docs docs
                }
            }
        }

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem "Diagram1" {
            include *
            autoLayout
        }

        theme default
    }

}