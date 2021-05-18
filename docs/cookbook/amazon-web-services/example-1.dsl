workspace {

    model {
        u = person "User"
        s = softwareSystem "Software System" {
            webapp = container "Web Application" "" "Spring Boot"
            database = container "Database" "" "Relational database schema"
        }

        u -> webapp "Uses"
        webapp -> database "Reads from and writes to"
        
        live = deploymentEnvironment "Live" {
            deploymentNode "Amazon Web Services" {
                deploymentNode "US-East-1" {
                    route53 = infrastructureNode "Route 53"
                    elb = infrastructureNode "Elastic Load Balancer"

                    deploymentNode "Amazon EC2" {
                        deploymentNode "Ubuntu Server" {
                            webApplicationInstance = containerInstance webapp
                        }
                    }

                    deploymentNode "Amazon RDS" {
                        deploymentNode "MySQL" {
                            containerInstance database
                        }
                    }
                }
            }
            
            route53 -> elb "Forwards requests to" "HTTPS"
            elb -> webApplicationInstance "Forwards requests to" "HTTPS"
        }
    }

    views {
        deployment s live {
            include *
            autoLayout lr
        }
    }
    
}