workspace {
    model {
        de = deploymentEnvironment "DeploymentEnvironment"

        !ref de {
            dn = deploymentNode "DeploymentNode"
        }
    }
}