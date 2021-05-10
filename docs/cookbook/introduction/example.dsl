workspace {

    model {
        clinicEmployee = person "Clinic Employee"
        springPetClinic = softwareSystem "Spring PetClinic"{
            webApplication = container "Web Application"
            database = container "Database"
        }

        clinicEmployee -> webApplication "Uses"
        webApplication -> database "Reads from and writes to"
    }

    views {
        systemContext springPetClinic {
            include *
            autolayout
        }

        container springPetClinic {
            include *
            autolayout
        }
   }
    
}
