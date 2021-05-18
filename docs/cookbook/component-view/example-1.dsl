workspace {

    model {
        u = person "User"
        s = softwareSystem "Software System" {
            webapp = container "Web Application" {
                c1 = component "Component 1"    
                c2 = component "Component 2"    
            }
            database = container "Database"
        }

        u -> c1 "Uses"
        c1 -> c2 "Uses"
        c2 -> database "Reads from and writes to"
    }

    views {
        component webapp {
            include *
            autoLayout lr
        }
    }
    
}