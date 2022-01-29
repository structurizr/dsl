workspace extends https://raw.githubusercontent.com/structurizr/dsl/master/examples/extend/workspace.dsl {
    name "A new name"
    description "A new description"

    model {
        softwareSystem = softwareSystem "Software System" {
            webapp = container "Web Application"
        }
    }

    views {
        systemContext softwareSystem "SystemContext" "An example of a System Context diagram." {
            include *
            autoLayout
        }

        styles {
            element "Software System" {
                background #1168bd
                color #ffffff
            }
            element "Person" {
                shape person
                background #08427b
                color #ffffff
            }
        }
    }
    
}