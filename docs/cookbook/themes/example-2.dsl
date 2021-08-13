workspace {

    model {
        softwareSystem "Authentication Service" {
            tags "Microsoft Azure - Azure Active Directory"
        }
    }

    views {
        systemLandscape {
            include *
            autoLayout lr
        }
        
        styles {
            element "Software System" {
                background #ffffff
                shape RoundedBox
            }
        }
        
        theme https://static.structurizr.com/themes/microsoft-azure-2021.01.26/theme.json
    }
    
}