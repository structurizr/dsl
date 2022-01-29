workspace {

    model {
        softwareSystem = softwareSystem "Software System" {
            database = container "Database"
            api = container "Service API" {
                -> database "Uses"
            }
        }

        deploymentEnvironment "Example 1" {
            deploymentNode "Server 1" {
                containerInstance api
                containerInstance database
            }
            deploymentNode "Server 2" {
                containerInstance api
                containerInstance database
            }
        }

        deploymentEnvironment "Example 2" {
            serviceInstance1 = deploymentGroup "Service Instance 1"
            serviceInstance2 = deploymentGroup "Service Instance 2"
            deploymentNode "Server 1" {
                containerInstance api serviceInstance1
                containerInstance database serviceInstance1
            }
            deploymentNode "Server 2" {
                containerInstance api serviceInstance2
                containerInstance database serviceInstance2
            }
        }
    }

    views {
        deployment * "Example 1" {
            include *
            autolayout
        }

        deployment * "Example 2" {
            include *
            autolayout
        }
    }

}