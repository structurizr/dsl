workspace {

    !identifiers hierarchical

    model {
       a = person "A"
       b = softwareSystem "B"{
             c = container "C" {
                 d = component "D"
                 a = component "A"

                 d -> a
           }
       }
    }
}