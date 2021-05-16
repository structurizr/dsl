workspace {

    model {
        softwareSystem = softwareSystem "Software System" {
            service1 = group "Service 1" {
                service1Api = container "Service 1 API"
                service1Database = container "Service 1 Database"

                service1Api -> service1Database "Reads from and writes to"
            }
            service2 = group "Service 2" {
                service2Api = container "Service 2 API"
                service2Database = container "Service 2 Database"

                service2Api -> service2Database "Reads from and writes to"
            }
        }

        live = deploymentEnvironment "Live" {
            deploymentNode "Server 1" {
                containerInstance service1Api
                containerInstance service1Database
            }
            deploymentNode "Server 2" {
                containerInstance service2Api
                containerInstance service2Database
            }
        }

        service1Api -> service2Api "Uses"
    }

    views {
        container softwareSystem {
            include service1 service2
            autolayout
        }

        deployment softwareSystem live {
            include service1 service2
            autolayout
        }
    }
    
}