workspace extends https://raw.githubusercontent.com/structurizr/dsl/master/src/test/dsl/extend/workspace.dsl {

    model {
        !ref softwareSystem1 {
            webapp = container "Web Application"
        }

        user -> softwareSystem1 "Uses"
        softwareSystem3.webapp -> softwareSystem3.db
    }

}