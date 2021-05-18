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
                tags "Amazon Web Services - Cloud"
                
                deploymentNode "US-East-1" {
                    tags "Amazon Web Services - Region"
                
                    route53 = infrastructureNode "Route 53" {
                        tags "Amazon Web Services - Route 53"
                    }
                    elb = infrastructureNode "Elastic Load Balancer" {
                        tags "Amazon Web Services - Elastic Load Balancing"
                    }

                    deploymentNode "Amazon EC2" {
                        tags "Amazon Web Services - EC2"
                        
                        deploymentNode "Ubuntu Server" {
                            webApplicationInstance = containerInstance webapp
                        }
                    }

                    deploymentNode "Amazon RDS" {
                        tags "Amazon Web Services - RDS"
                        
                        deploymentNode "MySQL" {
                            tags "Amazon Web Services - RDS MySQL instance"
                            
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

        theme https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json
    }
    
}