workspace {

  model {
        a = softwareSystem "A"
        b = softwareSystem "B" {
            c = container "C"
        }

        c -> a
        b -> a
  }

}