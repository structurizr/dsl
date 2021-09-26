workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" {
            !docs docs
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