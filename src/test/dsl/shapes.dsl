workspace "Shapes" "An example of all shapes available in Structurizr." {

    model {
        softwareSystem "Box" "" "Box" 
        softwareSystem "RoundedBox" "" "RoundedBox" 
        softwareSystem "Diamond" "" "Diamond" 
        softwareSystem "Circle" "" "Circle" 
        softwareSystem "Ellipse" "" "Ellipse" 
        softwareSystem "Hexagon" "" "Hexagon" 
        softwareSystem "Folder" "" "Folder" 
        softwareSystem "Cylinder" "" "Cylinder" 
        softwareSystem "Pipe" "" "Pipe" 
        softwareSystem "WebBrowser" "" "Web Browser" 
        softwareSystem "Mobile Device Portrait" "" "Mobile Device Portrait" 
        softwareSystem "Mobile Device Landscape" "" "Mobile Device Landscape" 
        softwareSystem "Component" "" "Component" 
	    person "Person"
        softwareSystem "Robot" "" "Robot" 
    }

    views {
        systemLandscape "shapes" "An example of all shapes available in Structurizr." {
            include *
        }

        styles {
            element "Element" {
                width "650" 
                height "400" 
                background "#438dd5" 
                color "#ffffff" 
                fontSize "34" 
                metadata "false" 
                description "false" 
            }
            element "Box" {
                shape "Box" 
            }
            element "RoundedBox" {
                shape "RoundedBox" 
            }
            element "Diamond" {
                shape "Diamond"
            }
            element "Circle" {
                shape "Circle" 
            }
            element "Ellipse" {
                shape "Ellipse" 
            }
            element "Hexagon" {
                shape "Hexagon" 
            }
            element "Folder" {
                shape "Folder" 
            }
            element "Cylinder" {
                shape "Cylinder" 
            }
            element "Pipe" {
                shape "Pipe" 
            }
            element "Web Browser" {
                shape "WebBrowser" 
            }
            element "Mobile Device Portrait" {
                shape "MobileDevicePortrait" 
                width "400" 
                height "650" 
            }
            element "Mobile Device Landscape" {
                shape "MobileDeviceLandscape" 
            }
            element "Component" {
                shape "Component" 
            }
            element "Person" {
                shape "Person" 
                width "550" 
            }
            element "Robot" {
                shape "Robot" 
                width "550" 
            }
        }

    }

}
