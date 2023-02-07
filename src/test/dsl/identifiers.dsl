workspace {

    !identifiers hierarchical

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" {
            container = container "Container" {
                rel = user -> this "Uses"
            }
        }
    }

}