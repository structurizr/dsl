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

        user = person "User" "Description" "Tag" {
            url "https://structurizr.com"
            properties {
                "Name" "Value"
            }
            perspectives {
                "Security" "A description..."
            }
        }
        enterprise "Enterprise" {
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