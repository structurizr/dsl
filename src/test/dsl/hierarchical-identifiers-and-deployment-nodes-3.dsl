workspace {

    !identifiers hierarchical

    model {
        ss = softwareSystem "SS" {
            c = container "C"
        }

        live = deploymentEnvironment "Environment" {
            dn = deploymentNode "DN1" {
                ss = deploymentNode "DN2" {
                    c = deploymentNode "DN3" {
                        containerInstance ss.c
                    }
                }
            }
        }

    }

}