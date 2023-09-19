workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System"
    }

    views {
        dynamic * "key" {
            !script groovy {
                view.description = "Groovy"
            }
        }
    }

}