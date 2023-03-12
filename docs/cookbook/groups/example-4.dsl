workspace {

    model {
        properties {
            "structurizr.groupSeparator" "/"
        }

        group "Company 1" {
            group "Department 1" {
                a = softwareSystem "A"
            }

            group "Department 2" {
                b = softwareSystem "B"
            }
        }

        a -> b
    }

    views {
        systemLandscape {
            include *
            autolayout lr
        }

        styles {
            element "Group:Company 1/Department 1" {
                color #ff0000
            }
            element "Group:Company 1/Department 2" {
                color #0000ff
            }
        }
    }
    
}