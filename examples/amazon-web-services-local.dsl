workspace "Amazon Web Services Example" "An example AWS deployment architecture." {

    !identifiers hierarchical

    model {
        springPetClinic = softwaresystem "Spring PetClinic" "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets." "Spring Boot Application" {
            webApplication = container "Web Application" "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets." "Java and Spring Boot"
            database = container "Database" "Stores information regarding the veterinarians, the clients, and their pets." "Relational database schema" "Database"
            webApplication -> database "Reads from and writes to" "JDBC/SSL"
        }

        live = deploymentEnvironment "Live" {
            aws = deploymentNode "Amazon Web Services" "" "" "Amazon Web Services - Cloud" {
                region = deploymentNode "US-East-1" "" "" "Amazon Web Services - Region" {
                    route53 = infrastructureNode "Route 53" "" "" "Amazon Web Services - Route 53"
                    elb = infrastructureNode "Elastic Load Balancer" "" "" "Amazon Web Services - Elastic Load Balancing"

                    autoscalingGroup = deploymentNode "Autoscaling group" "" "" "Amazon Web Services - Auto Scaling" {
                        ec2 = deploymentNode "Amazon EC2" "" "" "Amazon Web Services - EC2" {
                            webApplicationInstance = containerInstance springPetClinic.webApplication
                            elb -> webApplicationInstance "Forwards requests to" "HTTPS"
                        }
                    }

                    rds = deploymentNode "Amazon RDS" "" "" "Amazon Web Services - RDS" {
                        mysql = deploymentNode "MySQL" "" "" "Amazon Web Services - RDS MySQL instance" {
                            databaseInstance = containerInstance springPetClinic.database
                        }
                    }

                    route53 -> elb "Forwards requests to" "HTTPS"
                }
            }
        }
    }

    views {
        deployment springPetClinic "Live" "AmazonWebServicesDeployment" {
            include *
            autolayout lr

            animation {
                live.aws.region.route53
                live.aws.region.elb
                live.aws.region.autoscalingGroup.ec2.webApplicationInstance
                live.aws.region.rds.mysql.databaseInstance
            }
        }

        styles {
            element "Element" {
                shape roundedbox
                background #ffffff
            }
            element "Database" {
                shape cylinder
            }
            element "Infrastructure Node" {
                shape roundedbox
            }
        }

        themes https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json
    }
}