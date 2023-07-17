workspace {

    model {
        softwareSystem "A" {
            a = container "A"
        }

        softwareSystem "B" {
            b = container "B"
        }

        r = a -> b
    }

    views {
        systemLandscape {
            include *
            exclude *->*
            include r
        }
    }

}