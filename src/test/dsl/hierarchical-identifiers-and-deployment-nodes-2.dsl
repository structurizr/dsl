workspace {

    !identifiers hierarchical

    model {
        ss = softwareSystem "SS"

        live = deploymentEnvironment "Environment" {
            dn = deploymentNode "DN1" {
                ss = deploymentNode "DN2" {
                    softwareSystemInstance ss
                }
            }
        }

    }

}