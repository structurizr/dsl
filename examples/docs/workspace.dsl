workspace {

    model {
        user = person "User"

        softwareSystem = softwareSystem "Software System" {
            user -> this "Uses"
            !docs docs
        }
    }

    views {
        systemContext softwareSystem "Diagram1" {
            include *
            autolayout
        }
    }

}