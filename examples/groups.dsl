workspace {

    model {
        group "Consumers - Group 1" {
            consumerA = softwareSystem "Consumer A"
            consumerB = softwareSystem "Consumer B"
        }
        group "Consumers - Group 2" {
            consumerC = softwareSystem "Consumer C"
            consumerD = softwareSystem "Consumer D"
        }

        softwareSystem = softwareSystem "Software System" "My software system." {
            group "Service 1" {
                service1Database = container "Service 1 Database"
                service1Api = container "Service 1 API"
            }
            group "Service 2" {
                service2Database = container "Service 2 Database"
                service2Api = container "Service 2 API"
            }
        }

        consumerA -> service1Api "Uses"
        consumerB -> service1Api "Uses"
        consumerC -> service1Api "Uses"
        consumerD -> service1Api "Uses"

        service1Api -> service1Database "Reads from and writes to"
        service2Api -> service2Database "Reads from and writes to"
        service1Api -> service2Api "Uses"
    }

    views {
        systemContext softwareSystem "SystemContext" {
            include *
            autoLayout
        }

        container softwareSystem "Containers" {
            include *
            autolayout
        }

        styles {
            element "Person" {
                shape person
            }
        }
    }
    
}