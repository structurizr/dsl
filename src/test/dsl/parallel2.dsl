workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"
        d = softwareSystem "D"

        a -> b
        b -> c
        b -> d

    }

    views {

        dynamic * {
            a -> b "request"
            {
                {
                    b -> c
                }
                {
                    b -> d
                }
            }
            b -> a "response"

            autoLayout
        }
    }

}