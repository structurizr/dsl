workspace {

    model {
        u = person "User"
        s = softwareSystem "Software System"

        u -> s "Uses"
    }

    views {
        systemContext s {
            include *
            autoLayout lr
        }
    }
    
}