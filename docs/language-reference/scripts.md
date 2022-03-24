# Scripts

Scripts are like plugins, except they don't need to be compiled before use. JavaScript(*), Kotlin, Groovy, and Ruby are supported out of the box, and you can add more languages via the Java Scripting API. The workspace (again from the [Structurizr for Java library](https://github.com/structurizr/java)) is bound to a variable named `workspace`. Scripts can be used at any point in the DSL.

__Please note that `!script` is currently an experimental feature.__
(*) Nashorn (the JVM JavaScript engine) is deprecated, see [https://openjdk.java.net/jeps/372](https://openjdk.java.net/jeps/372) for details.

## Inline scripts

To use an inline script, use the `!script` keyword followed by the language you'd like to use (`groovy`, `kotlin`, or `javascript`). For example, the following Kotlin script will create the default set of views, without automatic layout enabled.

```
!script kotlin {
  workspace.views.createDefaultViews()
  workspace.views.views.forEach { it.disableAutomaticLayout() }
}
```

Please note that inline scripts cannot have a line that only contains a closing `}` character.

## External scripts

To use an external script, create a script file next to your DSL file (e.g. `script.kts`).

```
workspace.views.createDefaultViews()
workspace.views.views.forEach { it.disableAutomaticLayout() }
```

You can then use your script from the DSL using the `!script` keyword.

 ```
  !script script.kts
  ```

The following file extensions are recognised:

- `.groovy` (Groovy)
- `.kts` (Kotlin)
- `.rb` (Ruby)
- `.js` (JavaScript)

