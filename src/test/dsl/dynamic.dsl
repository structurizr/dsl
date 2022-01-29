workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"

        a -> b "Sends data to"
    }

    views {
        dynamic * {
            // with this example, the relationship uses the same description as defined in the static model
            a -> b
            autoLayout
        }

        dynamic * {
            // with this example, the relationship description is overriden to describe a particular feature/use case/etc
            a -> b "Sends customer data to"
            autoLayout
        }
    }

}