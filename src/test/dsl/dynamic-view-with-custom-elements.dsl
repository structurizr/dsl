workspace {

    model {
        a = element "A"
        b = softwareSystem "B"
        c = element "C"

        a -> b
        b -> c
    }

    views {
        dynamic * {
            a -> b
            b -> c
        }
    }

}