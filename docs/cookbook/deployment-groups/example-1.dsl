workspace {

    model {
        softwareSystem = softwareSystem "Software System" {
            database = container "Database Schema"
            api = container "Service API" {
                -> database "Uses"
            }
        }

        production = deploymentEnvironment "Production" {
            deploymentNode "Server 1" {
                containerInstance api
                deploymentNode "Database Server" {
                    containerInstance database
                }
            }
            deploymentNode "Server 2" {
                containerInstance api
                deploymentNode "Database Server" {
                    containerInstance database
                }
            }
        }
    }

    views {
        deployment * production {
            include *
            autolayout
        }
    }

}