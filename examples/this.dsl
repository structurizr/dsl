workspace {

    model {
        custom = element "Element"

        s = softwareSystem "Software System" {
            custom -> this

            c = container "Container" {
                custom -> this

                component "Component" {
                    custom -> this
                }
            }
        }

        live = deploymentEnvironment "live" {
            deploymentNode "Live" {
                in = infrastructureNode "Infrastructure Node" {
                    custom -> this
                }

                dn = deploymentNode "Deployment Node" {
                    in -> this

                    softwareSystemInstance s {
                        in -> this
                    }

                    containerInstance c {
                        in -> this
                    }
                }
            }
        }
    }

    views {
        systemLandscape {
            include *
            include custom
            autolayout
        }

        systemContext s {
            include *
            include custom
            autolayout
        }

        container s {
            include *
            include custom
            autolayout
        }

        component c {
            include *
            include custom
            autolayout
        }

        deployment * live {
            include *
            include custom
            autolayout
        }
    }

}