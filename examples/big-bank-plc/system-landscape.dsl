!constant INTERNET_BANKING_SYSTEM_INCLUDE "summary.dsl"

workspace "Big Bank plc - System Landscape" "The system landscape for Big Bank plc." {

    model {
        !include model/people-and-software-systems.dsl
    }

    views {
        systemlandscape "SystemLandscape" {
            include *
        }

        styles {
            !include views/styles-people.dsl

            element "Software System" {
                background #999999
                color #ffffff
            }
        }
    }
}