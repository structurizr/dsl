workspace {

    !identifiers hierarchical

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        
        a -> b "Gets data X from"
    }
    
    views {
        systemLandscape "SystemLandscape" {
            include *
            autolayout lr
        }
    }
    
}