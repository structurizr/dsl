workspace {

    model {
        user = person "User" "A user of my software system."
        softwareSystem = softwareSystem "Software System" "My software system."
        softwareSystem2 = softwareSystem "Software System2" "My software system."
        softwareSystem3 = softwareSystem "Software System3" "My software system."

        uss = user -> softwareSystem "Uses"
        uss2 = user -> softwareSystem2 "Uses"
        uss3 = user -> softwareSystem3 "Uses"
        ss2ss3 = softwareSystem2 -> softwareSystem3 "Uses"
    }

    views {
        systemContext softwareSystem {
            disableAutoAddRelations
            include user softwareSystem2 uss uss2
            autoLayout
        }
    }
    
}