workspace {

    model {
        group "Users - Group 1" {
            userA = person "User A"
            userB = person "User B"
        }
        group "Users - Group 2" {
            userC = person "User C"
            userD = person "User D"
        }
        softwareSystem = softwareSystem "Software System" "My software system."

        userA -> softwareSystem "Uses"
        userB -> softwareSystem "Uses"
        userC -> softwareSystem "Uses"
        userD -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem "SystemContext" {
            include *
            autoLayout
        }

        styles {
            element "Person" {
                shape person
            }
        }
    }
    
}