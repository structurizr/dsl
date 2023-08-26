workspace extends workspace.dsl {

    model {
        !ref softwareSystem1 {
            webapp = container "Web Application"
        }

        user -> softwareSystem1 "Uses"
    }

}