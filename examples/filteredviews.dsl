workspace "FilteredDemo" "This is an example of Filtered views" {

    # This will render the two diagrams at https://structurizr.com/help/filtered-views

    model {

        user = person "Customer" "A description of the user."
        sysa = softwareSystem "Software System A" "A description of software system A."

        user -> sysa "Uses for tasks 1 and 2" "" Current

        sysb = softwareSystem "Software System B" "A description of software system B." Future

        user -> sysa "Uses for task 1" "" Future
        user -> sysb "Uses for task 2" "" Future

    }

    views {

        systemLandscape FullLandscape "System Landscape, current and future" {
            include *
        }

        filtered FullLandscape exclude Future CurrentLandscape "The current system landscape."
        filtered FullLandscape exclude Current FutureLandscape "The future state system landscape after Software System B is live."

        styles {
            element "Software System" {
                background #91a437
                shape RoundedBox
            }

            element "Person" {
                background #6a7b15
                shape Person
            }
        }

    }
}
