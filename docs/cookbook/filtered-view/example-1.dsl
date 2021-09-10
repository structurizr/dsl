workspace {

    model {
        a = softwareSystem "A" {
            tags "Tag 1"
        }
        b = softwareSystem "B" {
            tags "Tag 2"
        }
        c = softwareSystem "C" {
            tags "Tag 3"
        }
        
        a -> b
        b -> c
    }
    
    views {
        systemLandscape "landscape" {
            include *
            autolayout lr
        }
    }
        
}