workspace extends https://raw.githubusercontent.com/structurizr/dsl/master/examples/amazon-web-services.dsl {

    model {

        region = ref "DeploymentNode://Live/Amazon Web Services/US-East-1" {
            deploymentNode "New deployment node" {
                infrastructureNode "New infrastructure node" {
                    -> route53
                }
            }
        }

    }

    views {
        deployment * "Live" {
            include region
            autolayout lr
        }
    }

}