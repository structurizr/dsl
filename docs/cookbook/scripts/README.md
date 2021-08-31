# Scripts

The `!script` keyword provides a way to run scripts written in Groovy, Kotlin, Ruby, and JavaScript. This gives you access to the underlying Structurizr for Java workspace, for when you need to do something not supported by the DSL. Here are some useful scripts.

#### Create the default views, without automatic layout

```
!script groovy {
    workspace.views.createDefaultViews()
    workspace.views.views.each { it.disableAutomaticLayout() }
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
