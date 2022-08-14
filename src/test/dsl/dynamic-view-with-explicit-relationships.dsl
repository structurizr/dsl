workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"

        r1 = a -> b "Sync" {
            tags "Sync"
        }

        r2 = a -> b "Async" {
            tags "Async"
        }
    }

    views {
        systemLandscape {
            include *
            autoLayout
        }

        dynamic * {
            r2 "Async"
            autoLayout
        }

        styles {
            relationship "Sync" {
                style solid
            }
            relationship "Async" {
                style dashed
            }
        }
    }

}