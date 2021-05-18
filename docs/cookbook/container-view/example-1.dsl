workspace {

    model {
        u = person "User"
        s = softwareSystem "Software System" {
            webapp = container "Web Application"
            database = container "Database"
        }

        u -> webapp "Uses"
        webapp -> database "Reads from and writes to"
    }

    views {
        container s {
            include *
            autoLayout lr
        }
    }
    
}