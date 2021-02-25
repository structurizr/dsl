!constant ORGANISATION_NAME "Organisation"
!constant GROUP_NAME "Group"

workspace "Name" "Description" {

    /*
        multi-line comment
    */

    /**
        multi-line comment
    */

    /* multi-line comment on single line */

    /* multi-line comment
        on two lines */

    # single line comment
    // single line comment

    model {

        box1 = element "Box 1" "Metadata" "Description" "Tag"
        box2 = element "Box 2" "Metadata" "Description" "Tag"
        box1 -> box2

        user = person "User" "Description" "Tag" {
            url "https://structurizr.com"
            properties {
                "Name" "Value"
            }
            perspectives {
                "Security" "A description..."
            }
        }

        enterprise "${ORGANISATION_NAME} - ${GROUP_NAME}" {
            softwareSystem = softwareSystem "Software System" "Description"  "Tag" {
                webApplication = container "Web Application" "Description" "Technology" "Tag" {
                    homePageController = component "HomePageController" "Description" "Spring MVC Controller" "Tag" {
                        url "https://structurizr.com"
                        properties {
                            "Name" "Value"
                        }
                        perspectives {
                            "Security" "A description..."
                        }
                    }

                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                }

                url "https://structurizr.com"
                properties {
                    "Name" "Value"
                }
                perspectives {
                    "Security" "A description..."
                }
            }

            softwareSystem "E-mail System" "Description" "Tag"
        }

        user -> HomePageController "Visits" "HTTPS" "Tag"

        developmentEnvironment = deploymentEnvironment "Development" {
            deploymentNode "Amazon Web Services" "Description" "Technology" "Tag" {
                softwareSystemInstance softwareSystem {
                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                    healthCheck "Check 1" "https://example.com/health"
                    healthCheck "Check 2" "https://example.com/health" 60
                    healthCheck "Check 2" "https://example.com/health" 120 1000
                }
            }
        }

        deploymentEnvironment "Live" {
            deploymentNode "Amazon Web Services" "Description" "Technology" "Tag" {

                infrastructureNode "Elastic Load Balancer" "Description" "Technology" "Tag" {
                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                }

                deploymentNode "Amazon Web Services - EC2" "Description" "Technology" "Tag" {
                    containerInstance webApplication {
                        url "https://structurizr.com"
                        properties {
                            "Name" "Value"
                        }
                        perspectives {
                            "Security" "A description..."
                        }
                        healthCheck "Check 1" "https://example.com/health"
                        healthCheck "Check 2" "https://example.com/health" 60
                        healthCheck "Check 2" "https://example.com/health" 120 1000
                    }
                }

                url "https://structurizr.com"
                properties {
                    "Name" "Value"
                }
                perspectives {
                    "Security" "A description..."
                }

            }
        }
    }
         
    views {

        custom "CustomDiagram" "Title" "Description" {
            include box1 box2

            animation {
                box1
                box2
            }

            autolayout
        }

        systemLandscape "SystemLandscape" "Description" {
            include *
            autoLayout
        }

        systemContext softwareSystem "SystemContext" "Description" {
            include *
            autoLayout
        }

        container softwareSystem "Containers" "Description" {
            include *
            autoLayout
        }

        component webApplication "Components" "Description" {
            include *
            autoLayout
        }

        dynamic webApplication "Dynamic" "Description" {
            user -> homePageController "Requests via web browser"
            autoLayout
        }

        deployment * developmentEnvironment "Deployment-Development" "Description" {
            include *
            autoLayout
        }

        deployment * "Live" "Deployment-Live" "Description" {
            include *
            autoLayout
        }

        styles {
            element "Element" {
                shape roundedbox
                icon logo.png
                width 450
                height 300
                background #ffffff
                color #000000
                colour #000000
                stroke #777777
                fontSize 24
                border solid
                opacity 50
                metadata false
                description false
            }

            relationship "Relationship" {
                thickness 2
                color #777777
                colour #777777
                dashed true
                routing curved
                fontSize 24
                width 400
                position 50
                opacity 50
            }
        }

        themes https://example.com/theme1 https://example.com/theme2 https://example.com/theme3

        branding {
            logo logo.png
            font "Example" https://example/com/font
        }

        terminology {
            enterprise "Enterprise"
            person "Person"
            softwareSystem "Software System"
            container "Container"
            component "Component"
            deploymentNode "Deployment Node"
            infrastructureNode "Infrastructure Node"
            relationship "Relationship"
        }
    }

}