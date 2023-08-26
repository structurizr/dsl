workspace {

    !identifiers hierarchical

    model {
        user = person "User"

        softwareSystem1 = softwareSystem "Software System 1"

        softwareSystem "Software System 2"

        softwareSystem3 = softwareSystem "Software System 3" {
            webapp = container "Web Application"
            db = container "Database"
        }
    }

}