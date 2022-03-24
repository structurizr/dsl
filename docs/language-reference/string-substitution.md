# String Substitution

String substitution will take place on any text specified using the `${NAME}`
syntax, where `NAME` corresponds to a [constant](#constants) or an environment
variable. For example:

```
!constant ORGANISATION_NAME "Organisation"
!constant GROUP_NAME "Group"

workspace {

    model {
        enterprise "${ORGANISATION_NAME} - ${GROUP_NAME}" {
            user = person "User"
        }
    }

}
```

If a named constant or environment variable cannot be found, the string will not
be substituted. Names may only contain the following characters: `a-zA-Z0-9-_.`

