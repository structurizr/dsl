workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"

        a -> b
        b -> c {
            tags "Tag 1"
        }
    }

    views {
        systemLandscape {
            include *
            autolayout lr
        }
        
        styles {
            relationship "Tag 1" {
                color #ff0000
                dashed false
            }
        }
    }
    
}