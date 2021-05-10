# System Context diagram

A [System Context diagram](https://c4model.com/#SystemContextDiagram) is a good starting point for diagramming and documenting a software system, allowing you to step back and see the big picture. It shows the software system in the centre, surrounded by its users and the other systems that it interacts with.

The following DSL shows an example of a System Context diagram.

```
workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System"

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem {
            include *
            autoLayout
        }
    }
    
}
```

[View this example online]()

