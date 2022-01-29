workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System"

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem {
            include *
            exclude "* -> element.tag==Software System"
            autolayout
        }

        theme default
    }

}