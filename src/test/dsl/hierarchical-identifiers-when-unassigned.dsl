workspace {

    !identifiers hierarchical

    model {
        softwareSystem "A" {
            container "B" {
                component "C"
            }
        }

        deploymentEnvironment "Environment" {
            deploymentNode "Deployment Node" {
                infrastructureNode "Infrastructure Node"
            }
        }

    }
}