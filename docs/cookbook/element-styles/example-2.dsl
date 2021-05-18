workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"

        a -> b
        b -> c
    }

    views {
        systemLandscape {
            include *
            autolayout lr
        }
        
        styles {
            element "Element" {
                background #1168bd
                color #ffffff
                shape RoundedBox
            }
        }
    }
    
}