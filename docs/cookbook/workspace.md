# Workspace

In Structurizr terminology, a "workspace" is a wrapper for a software architecture model (elements and relationships) and views.

```
workspace "Name" "Description" {

    model {
    }
    
    views {
    }
    
}
```

A workspace can be given a name and description, although these are only used by the [Structurizr cloud service and on-premises installation](https://structurizr.com) - you don't need to specify a name/description if you're exporting views to one of the export formats (PlantUML, Mermaid, etc).