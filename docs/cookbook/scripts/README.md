# Scripts

The `!script` keyword provides a way to run scripts written in Groovy, Kotlin, Ruby, and JavaScript.
This gives you access to the underlying [Workspace](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/Workspace.java) via a variable named `workspace`,
for when you need to do something not supported by the DSL.
Other variables (`element`, `relationship`, `view`) are available depending on where the script is defined; see the [DSL language reference - !script](../../language-reference.md#script) for more details.
Here are some useful scripts.

#### Create the default views, without automatic layout

```
!script groovy {
    workspace.views.createDefaultViews()
    workspace.views.views.each { it.disableAutomaticLayout() }
}
```

#### Programmatically add elements to a view

```
workspace {

    model {
        group "Group 1" {
            a = softwareSystem "A" {
                tags "Tag 1"
            }
            b = softwareSystem "B" {
                tags "Tag 2"
            }
        }
        group "Group 2" {
            c = softwareSystem "C" {
                tags "Tag 1"
            }
            d = softwareSystem "D" {
                tags "Tag 2"
            }
        }
    }

    views {
        systemLandscape "key" {
            !script groovy {
                workspace.model.softwareSystems.findAll { it.group == "Group 1" && it.hasTag("Tag 1") }.each{ view.add(it); };
            }

            autolayout
        }
    }

}
```

#### Run Graphviz locally

```
!script groovy {
    new com.structurizr.graphviz.GraphvizAutomaticLayout().apply(workspace);
}
```

(this requires Graphviz to be installed locally)

## Links

- [DSL language reference - !script](https://github.com/structurizr/dsl/blob/master/docs/language-reference.md#scripts)
- [Structurizr for Java source code](https://github.com/structurizr/java/tree/master/structurizr-core/src/com/structurizr)
