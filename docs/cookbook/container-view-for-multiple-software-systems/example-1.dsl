workspace {

    model {
        s1 = softwareSystem "Software System 1" {
            c1 = container "Container 1"
        }

        s2 = softwareSystem "Software System 2" {
            c2 = container "Container 2"
        }

        c1 -> c2
    }

    views {
        container s1 {
            include *
            autoLayout lr
        }
    }
    
}