workspace {

    model {
        a = softwareSystem "A" {
            perspectives {
                "Ownership" "Team 1"
            }
        }

        b = softwareSystem "B" {
            perspectives {
                "Ownership" "Team 2"
            }
        }
    }

    views {
        systemLandscape {
            include *
            autoLayout
        }
    }
    
}