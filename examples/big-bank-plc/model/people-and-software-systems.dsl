customer = person "Personal Banking Customer" "A customer of the bank, with personal bank accounts." "Customer"

enterprise "Big Bank plc" {
    supportStaff = person "Customer Service Staff" "Customer service staff within the bank." "Bank Staff"
    backoffice = person "Back Office Staff" "Administration and support staff within the bank." "Bank Staff"

    mainframe = softwaresystem "Mainframe Banking System" "Stores all of the core banking information about customers, accounts, transactions, etc." "Existing System"
    email = softwaresystem "E-mail System" "The internal Microsoft Exchange e-mail system." "Existing System"
    atm = softwaresystem "ATM" "Allows customers to withdraw cash." "Existing System"

    internetBankingSystem = softwaresystem "Internet Banking System" "Allows customers to view information about their bank accounts, and make payments." {
        !include "internet-banking-system/${INTERNET_BANKING_SYSTEM_INCLUDE}"
    }
}

# relationships between people and software systems
customer -> internetBankingSystem "Views account balances, and makes payments using"
internetBankingSystem -> mainframe "Gets account information from, and makes payments using"
internetBankingSystem -> email "Sends e-mail using"
email -> customer "Sends e-mails to"
customer -> supportStaff "Asks questions to" "Telephone"
supportStaff -> mainframe "Uses"
customer -> atm "Withdraws cash using"
atm -> mainframe "Uses"
backoffice -> mainframe "Uses"