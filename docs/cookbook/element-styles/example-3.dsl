workspace {

    model {
        a = softwareSystem "A" {
            tags "Tag 1"
        }
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
            element "Tag 1" {
                background #1168bd
                color #ffffff
                shape RoundedBox
            }
        }
    }
    
}