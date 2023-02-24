workspace {

    !identifiers hierarchical

    model {
        s = softwareSystem "Software System" {
            app1 = container "Application 1" {
                group "shared-library.jar" {
                    !include https://raw.githubusercontent.com/structurizr/dsl/master/docs/cookbook/shared-components/shared-library.dsl
                }

                c = component "Component" {
                    -> loggingComponent "Writes logs using"
                }
            }

            app2 = container "Application 2" {
                group "shared-library.jar" {
                    !include https://raw.githubusercontent.com/structurizr/dsl/master/docs/cookbook/shared-components/shared-library.dsl
                }

                c = component "Component" {
                    -> loggingComponent "Writes logs using"
                }
            }
        }
    }

    views {
        component s.app1 {
            include *
            autolayout lr
        }

        component s.app2 {
            include *
            autoLayout lr
        }

        styles {
            element "Container" {
                background #777777
            }
            element "Group" {
                colour #777777
            }
            element "Component" {
                background #dddddd
            }
            element "Shared Component" {
                background #f9f9f9
            }
        }
    }
    
}