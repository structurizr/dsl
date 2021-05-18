workspace {

    model {
        u = person "User"
        s = softwareSystem "Software System" {
            webapp = container "Web Application" "" "Spring Boot"
            database = container "Database" "" "Relational database schema"
        }

        u -> webapp "Uses"
        webapp -> database "Reads from and writes to"
        
        development = deploymentEnvironment "Development" {
            deploymentNode "Developer Laptop" {
                containerInstance webapp
                deploymentNode "MySQL" {
                    containerInstance database
                }
            }
        }
    }

    views {
        deployment * development {
            include *
            autoLayout lr
        }
    }
    
}