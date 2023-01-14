workspace {

    model {
        group "Company 1" {
            a = softwareSystem "A"
        }

        group "Company 2" {
            b = softwareSystem "B"
        }

        a -> b
    }

    views {
        systemLandscape {
            include *
            autolayout lr
        }

        styles {
            element "Group" {
                color #ff0000
            }
        }
    }
    
}