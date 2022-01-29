workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" {
            webapp = container "Web Application"
            bus = container "Message Bus"
            app1 = container "App 1"
            app2 = container "App 2"
        }

        user -> webapp "Updates details"
        webapp -> bus "Sends update event"
        bus -> app1 "Broadcasts update event"
        bus -> app2 "Broadcasts update event"
    }

    views {
        dynamic softwareSystem {
            user -> webapp
            webapp -> bus
            {
                bus -> app1
            }
            {
                bus -> app2
            }

            autoLayout
        }
    }

}