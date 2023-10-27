workspace extends https://raw.githubusercontent.com/structurizr/dsl/master/src/test/dsl/extend/workspace.json {

    model {
        // !extend with DSL identifier
        !extend softwareSystem1 {
            webapp1 = container "Web Application 1"
        }

        // !extend with canonical name
        !extend "SoftwareSystem://Software System 1" {
            webapp2 = container "Web Application 2"
        }

        user -> softwareSystem1 "Uses"
        softwareSystem3.webapp -> softwareSystem3.db
    }

}