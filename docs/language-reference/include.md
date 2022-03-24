# Includes

The `!include` keyword can be used to include one or more files, to provide some degree of modularity, and to reuse definition fragments between workspaces.
The content of any included files is simply inlined into the parent document, in the order the files are discovered.

```
!include <file|directory|url>
```

- file: a single local file, specified by a relative path, located within the same directory as the parent file or a subdirectory of it
- file: a local directory containing one or more DSL files, specified by a relative path, located within the same directory as the parent file or a subdirectory of it
- url: a HTTPS URL pointing to a single DSL file

Some examples are:

```
!include people.dsl
!include model/people.dsl
!include model
!include https://example.com/model/people.dsl
```

