workspace "Getting Started" "This is a model of my software system." {

    model {
        !include https://raw.githubusercontent.com/structurizr/dsl/master/src/test/dsl/include/model.dsl
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