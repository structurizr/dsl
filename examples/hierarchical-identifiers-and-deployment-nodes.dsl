 workspace {

    !identifiers hierarchical

    model {
        ss = softwareSystem "Software System"

        deploymentEnvironment "Live" {

            dn = deploymentNode "DN" {
                dn1 = deploymentNode "DN1" {
                    softwareSystemInstance ss
                }

                dn2 = deploymentNode "DN2" {
                    softwareSystemInstance ss
                }

                dn1 -> dn2
            }

            dn1 = deploymentNode "DN1" {
                softwareSystemInstance ss
            }

            dn2 = deploymentNode "DN2" {
                softwareSystemInstance ss
            }

            dn1 -> dn2
        }
    }

    views {
        deployment * "Live" {
            include *
        }
    }

}