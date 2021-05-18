workspace {

    model {
        customer = person "Customer"
        onlineBookStore = softwareSystem "Online book store" {
            webapp = container "Web Application"
            database = container "Database"
        }

        customer -> webapp "Browses and makes purchases using"
        webapp -> database "Reads from and writes to"
    }

    views {
        container onlineBookStore {
            include *
            autoLayout lr
        }
        
        dynamic onlineBookStore {
            title "Request past orders feature"
            customer -> webapp "Requests past orders from"
            webapp -> database "Queries for orders using"
            autoLayout lr
        }
        
        dynamic onlineBookStore {
            title "Browse top 20 books feature"
            customer -> webapp "Requests the top 20 books from"
            webapp -> database "Queries the top 20 books using"
            autoLayout lr
        }
    }
    
}