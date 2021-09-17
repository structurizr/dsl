workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        
        a -> b "Gets data X from"
    }
    
    views {
        systemLandscape "landscape" {
            include *
            autolayout lr
        }
    }
    
}