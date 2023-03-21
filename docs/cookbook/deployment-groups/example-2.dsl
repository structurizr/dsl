workspace {

    model {
        softwareSystem = softwareSystem "Software System" {
            database = container "Database Schema"
            api = container "Service API" {
                -> database "Uses"
            }
        }

        production = deploymentEnvironment "Production" {
            serviceInstance1 = deploymentGroup "Service instance 1"
            serviceInstance2 = deploymentGroup "Service instance 2"


            deploymentNode "Server 1" {
                containerInstance api serviceInstance1
                deploymentNode "Database Server" {
                    containerInstance database serviceInstance1
                }
            }
            deploymentNode "Server 2" {
                containerInstance api serviceInstance2
                deploymentNode "Database Server" {
                    containerInstance database serviceInstance2
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