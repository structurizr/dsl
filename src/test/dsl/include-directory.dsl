workspace {

    model {
        !constant SOFTWARE_SYSTEM_NAME "Software System 1"
        !include include/model/software-system/model.dsl

        !constant SOFTWARE_SYSTEM_NAME "Software System 2"
        !include include/model/software-system

        !constant SOFTWARE_SYSTEM_NAME "Software System 3"
        !include include/model
    }

}