workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"
        d = softwareSystem "D"
        e = softwareSystem "E"

        a -> b
        b -> c
        b -> d
        b -> e
    }

    views {

        dynamic * {
            a -> b "Makes a request to"
            {
                {
                    b -> c "Gets data from"
                }
                {
                    b -> d "Gets data from"
                }
            }
            b -> e "Sends data to"

            autoLayout
        }
    }

}