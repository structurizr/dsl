package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Location;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.WorkspaceUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StructurizrDslFormatterTests extends AbstractTests {

    @Test
    void test_emptyWorkspace() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Name\" \"Description\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model \n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_gettingstarted() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/getting-started.dsl"));
        Workspace workspace = parser.getWorkspace();

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Name\" \"Description\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        User = person \"User\" \n" +
                "        SoftwareSystem = softwareSystem \"Software System\" \n" +
                "        User -> SoftwareSystem \"Uses\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext SoftwareSystem \"SoftwareSystem-SystemContext\" {\n" +
                "            include User \n" +
                "            include SoftwareSystem \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        themes \"https://static.structurizr.com/themes/default/theme.json\" \n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_aws() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/amazon-web-services.dsl"));
        Workspace workspace = parser.getWorkspace();

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Amazon Web Services Example\" \"An example AWS deployment architecture.\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        SpringPetClinic = softwareSystem \"Spring PetClinic\" \"Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.\" \"Spring Boot Application\" {\n" +
                "            WebApplication = container \"Web Application\" \"Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.\" \"Java and Spring Boot\" \n" +
                "            Database = container \"Database\" \"Stores information regarding the veterinarians, the clients, and their pets.\" \"Relational database schema\" \"Database\" \n" +
                "        }\n" +
                "        SpringPetClinic.WebApplication -> SpringPetClinic.Database \"Reads from and writes to\" \"MySQL Protocol/SSL\" \n" +
                "\n" +
                "        Live = deploymentEnvironment \"Live\" {\n" +
                "            AmazonWebServices = deploymentNode \"Amazon Web Services\" \"\" \"\" \"Amazon Web Services - Cloud\" {\n" +
                "                USEast1 = deploymentNode \"US-East-1\" \"\" \"\" \"Amazon Web Services - Region\" {\n" +
                "                    Route53 = infrastructureNode \"Route 53\" \"\" \"\" \"Amazon Web Services - Route 53\" \n" +
                "                    ElasticLoadBalancer = infrastructureNode \"Elastic Load Balancer\" \"\" \"\" \"Amazon Web Services - Elastic Load Balancing\" \n" +
                "                    AmazonRDS = deploymentNode \"Amazon RDS\" \"\" \"\" \"Amazon Web Services - RDS\" {\n" +
                "                        MySQL = deploymentNode \"MySQL\" \"\" \"\" \"Amazon Web Services - RDS MySQL instance\" {\n" +
                "                            Database_1 = containerInstance SpringPetClinic.Database \n" +
                "                        }\n" +
                "                    }\n" +
                "                    Autoscalinggroup = deploymentNode \"Autoscaling group\" \"\" \"\" \"Amazon Web Services - Auto Scaling\" {\n" +
                "                        AmazonEC2 = deploymentNode \"Amazon EC2\" \"\" \"\" \"Amazon Web Services - EC2\" {\n" +
                "                            WebApplication_1 = containerInstance SpringPetClinic.WebApplication \n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Live.AmazonWebServices.USEast1.Route53 -> Live.AmazonWebServices.USEast1.ElasticLoadBalancer \"Forwards requests to\" \"HTTPS\" \n" +
                "        Live.AmazonWebServices.USEast1.ElasticLoadBalancer -> Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2.WebApplication_1 \"Forwards requests to\" \"HTTPS\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        deployment SpringPetClinic \"Live\" \"AmazonWebServicesDeployment\" {\n" +
                "            include Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2 \n" +
                "            include Live.AmazonWebServices.USEast1.AmazonRDS.MySQL \n" +
                "            include Live.AmazonWebServices.USEast1 \n" +
                "            autolayout lr 300 300 \n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Database\" {\n" +
                "                shape \"Cylinder\" \n" +
                "            }\n" +
                "            element \"Element\" {\n" +
                "                shape \"RoundedBox\" \n" +
                "                background \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Infrastructure Node\" {\n" +
                "                shape \"RoundedBox\" \n" +
                "            }\n" +
                "        }\n" +
                "        themes \"https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json\" \n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_bigbankplc() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/big-bank-plc.dsl"));
        Workspace workspace = parser.getWorkspace();

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Big Bank plc\" \"This is an example workspace to illustrate the key features of Structurizr, via the DSL, based around a fictional online banking system.\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        enterprise \"Big Bank plc\" {\n" +
                "            CustomerServiceStaff = person \"Customer Service Staff\" \"Customer service staff within the bank.\" \"Bank Staff\" \n" +
                "            BackOfficeStaff = person \"Back Office Staff\" \"Administration and support staff within the bank.\" \"Bank Staff\" \n" +
                "            MainframeBankingSystem = softwareSystem \"Mainframe Banking System\" \"Stores all of the core banking information about customers, accounts, transactions, etc.\" \"Existing System\" \n" +
                "            EmailSystem = softwareSystem \"E-mail System\" \"The internal Microsoft Exchange e-mail system.\" \"Existing System\" \n" +
                "            ATM = softwareSystem \"ATM\" \"Allows customers to withdraw cash.\" \"Existing System\" \n" +
                "            InternetBankingSystem = softwareSystem \"Internet Banking System\" \"Allows customers to view information about their bank accounts, and make payments.\" {\n" +
                "                WebApplication = container \"Web Application\" \"Delivers the static content and the Internet banking single page application.\" \"Java and Spring MVC\" \n" +
                "                APIApplication = container \"API Application\" \"Provides Internet banking functionality via a JSON/HTTPS API.\" \"Java and Spring MVC\" {\n" +
                "                    SignInController = component \"Sign In Controller\" \"Allows users to sign in to the Internet Banking System.\" \"Spring MVC Rest Controller\" \n" +
                "                    AccountsSummaryController = component \"Accounts Summary Controller\" \"Provides customers with a summary of their bank accounts.\" \"Spring MVC Rest Controller\" \n" +
                "                    ResetPasswordController = component \"Reset Password Controller\" \"Allows users to reset their passwords with a single use URL.\" \"Spring MVC Rest Controller\" \n" +
                "                    SecurityComponent = component \"Security Component\" \"Provides functionality related to signing in, changing passwords, etc.\" \"Spring Bean\" \n" +
                "                    MainframeBankingSystemFacade = component \"Mainframe Banking System Facade\" \"A facade onto the mainframe banking system.\" \"Spring Bean\" \n" +
                "                    EmailComponent = component \"E-mail Component\" \"Sends e-mails to users.\" \"Spring Bean\" \n" +
                "                }\n" +
                "                Database = container \"Database\" \"Stores user registration information, hashed authentication credentials, access logs, etc.\" \"Oracle Database Schema\" \"Database\" \n" +
                "                SinglePageApplication = container \"Single-Page Application\" \"Provides all of the Internet banking functionality to customers via their web browser.\" \"JavaScript and Angular\" \"Web Browser\" \n" +
                "                MobileApp = container \"Mobile App\" \"Provides a limited subset of the Internet banking functionality to customers via their mobile device.\" \"Xamarin\" \"Mobile App\" \n" +
                "            }\n" +
                "        }\n" +
                "        PersonalBankingCustomer = person \"Personal Banking Customer\" \"A customer of the bank, with personal bank accounts.\" \"Customer\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem \"Views account balances, and makes payments using\" \n" +
                "        InternetBankingSystem -> MainframeBankingSystem \"Gets account information from, and makes payments using\" \n" +
                "        InternetBankingSystem -> EmailSystem \"Sends e-mail using\" \n" +
                "        EmailSystem -> PersonalBankingCustomer \"Sends e-mails to\" \n" +
                "        PersonalBankingCustomer -> CustomerServiceStaff \"Asks questions to\" \"Telephone\" \n" +
                "        CustomerServiceStaff -> MainframeBankingSystem \"Uses\" \n" +
                "        PersonalBankingCustomer -> ATM \"Withdraws cash using\" \n" +
                "        ATM -> MainframeBankingSystem \"Uses\" \n" +
                "        BackOfficeStaff -> MainframeBankingSystem \"Uses\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem.WebApplication \"Visits bigbank.com/ib using\" \"HTTPS\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem.SinglePageApplication \"Views account balances, and makes payments using\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem.MobileApp \"Views account balances, and makes payments using\" \n" +
                "        InternetBankingSystem.WebApplication -> InternetBankingSystem.SinglePageApplication \"Delivers to the customer's web browser\" \n" +
                "        InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.SignInController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.AccountsSummaryController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.ResetPasswordController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.MobileApp -> InternetBankingSystem.APIApplication.SignInController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.MobileApp -> InternetBankingSystem.APIApplication \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.MobileApp -> InternetBankingSystem.APIApplication.AccountsSummaryController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.MobileApp -> InternetBankingSystem.APIApplication.ResetPasswordController \"Makes API calls to\" \"JSON/HTTPS\" \n" +
                "        InternetBankingSystem.APIApplication.SignInController -> InternetBankingSystem.APIApplication.SecurityComponent \"Uses\" \n" +
                "        InternetBankingSystem.APIApplication.AccountsSummaryController -> InternetBankingSystem.APIApplication.MainframeBankingSystemFacade \"Uses\" \n" +
                "        InternetBankingSystem.APIApplication.ResetPasswordController -> InternetBankingSystem.APIApplication.SecurityComponent \"Uses\" \n" +
                "        InternetBankingSystem.APIApplication.ResetPasswordController -> InternetBankingSystem.APIApplication.EmailComponent \"Uses\" \n" +
                "        InternetBankingSystem.APIApplication.SecurityComponent -> InternetBankingSystem.Database \"Reads from and writes to\" \"JDBC\" \n" +
                "        InternetBankingSystem.APIApplication -> InternetBankingSystem.Database \"Reads from and writes to\" \"JDBC\" \n" +
                "        InternetBankingSystem.APIApplication.MainframeBankingSystemFacade -> MainframeBankingSystem \"Makes API calls to\" \"XML/HTTPS\" \n" +
                "        InternetBankingSystem.APIApplication -> MainframeBankingSystem \"Makes API calls to\" \"XML/HTTPS\" \n" +
                "        InternetBankingSystem.APIApplication.EmailComponent -> EmailSystem \"Sends e-mail using\" \n" +
                "        InternetBankingSystem.APIApplication -> EmailSystem \"Sends e-mail using\" \n" +
                "\n" +
                "        Development = deploymentEnvironment \"Development\" {\n" +
                "            DeveloperLaptop = deploymentNode \"Developer Laptop\" \"\" \"Microsoft Windows 10 or Apple macOS\" {\n" +
                "                WebBrowser = deploymentNode \"Web Browser\" \"\" \"Chrome, Firefox, Safari, or Edge\" {\n" +
                "                    SinglePageApplication_1 = containerInstance InternetBankingSystem.SinglePageApplication \n" +
                "                }\n" +
                "                DockerContainerWebServer = deploymentNode \"Docker Container - Web Server\" \"\" \"Docker\" {\n" +
                "                    ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" {\n" +
                "                        WebApplication_1 = containerInstance InternetBankingSystem.WebApplication \n" +
                "                        APIApplication_1 = containerInstance InternetBankingSystem.APIApplication \n" +
                "                    }\n" +
                "                }\n" +
                "                DockerContainerDatabaseServer = deploymentNode \"Docker Container - Database Server\" \"\" \"Docker\" {\n" +
                "                    DatabaseServer = deploymentNode \"Database Server\" \"\" \"Oracle 12c\" {\n" +
                "                        Database_1 = containerInstance InternetBankingSystem.Database \n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "            BigBankplc = deploymentNode \"Big Bank plc\" \"\" \"Big Bank plc data center\" {\n" +
                "                bigbankdev001 = deploymentNode \"bigbank-dev001\" {\n" +
                "                    MainframeBankingSystem_1 = softwareSystemInstance MainframeBankingSystem \n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        Live = deploymentEnvironment \"Live\" {\n" +
                "            Customersmobiledevice = deploymentNode \"Customer's mobile device\" \"\" \"Apple iOS or Android\" {\n" +
                "                MobileApp_1 = containerInstance InternetBankingSystem.MobileApp \n" +
                "            }\n" +
                "            Customerscomputer = deploymentNode \"Customer's computer\" \"\" \"Microsoft Windows or Apple macOS\" {\n" +
                "                WebBrowser = deploymentNode \"Web Browser\" \"\" \"Chrome, Firefox, Safari, or Edge\" {\n" +
                "                    SinglePageApplication_1 = containerInstance InternetBankingSystem.SinglePageApplication \n" +
                "                }\n" +
                "            }\n" +
                "            BigBankplc = deploymentNode \"Big Bank plc\" \"\" \"Big Bank plc data center\" {\n" +
                "                bigbankweb = deploymentNode \"bigbank-web***\" \"\" \"Ubuntu 16.04 LTS\" {\n" +
                "                    ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" {\n" +
                "                        WebApplication_1 = containerInstance InternetBankingSystem.WebApplication \n" +
                "                    }\n" +
                "                }\n" +
                "                bigbankapi = deploymentNode \"bigbank-api***\" \"\" \"Ubuntu 16.04 LTS\" {\n" +
                "                    ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" {\n" +
                "                        APIApplication_1 = containerInstance InternetBankingSystem.APIApplication \n" +
                "                    }\n" +
                "                }\n" +
                "                bigbankdb01 = deploymentNode \"bigbank-db01\" \"\" \"Ubuntu 16.04 LTS\" {\n" +
                "                    OraclePrimary = deploymentNode \"Oracle - Primary\" \"\" \"Oracle 12c\" {\n" +
                "                        Database_1 = containerInstance InternetBankingSystem.Database \n" +
                "                    }\n" +
                "                }\n" +
                "                bigbankdb02 = deploymentNode \"bigbank-db02\" \"\" \"Ubuntu 16.04 LTS\" \"Failover\" {\n" +
                "                    OracleSecondary = deploymentNode \"Oracle - Secondary\" \"\" \"Oracle 12c\" \"Failover\" {\n" +
                "                        Database_1 = containerInstance InternetBankingSystem.Database \n" +
                "                    }\n" +
                "                }\n" +
                "                bigbankprod001 = deploymentNode \"bigbank-prod001\" {\n" +
                "                    MainframeBankingSystem_1 = softwareSystemInstance MainframeBankingSystem \n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Live.BigBankplc.bigbankdb01.OraclePrimary -> Live.BigBankplc.bigbankdb02.OracleSecondary \"Replicates data to\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemLandscape \"SystemLandscape\" {\n" +
                "            include PersonalBankingCustomer \n" +
                "            include CustomerServiceStaff \n" +
                "            include BackOfficeStaff \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include ATM \n" +
                "            include InternetBankingSystem \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        systemContext InternetBankingSystem \"SystemContext\" {\n" +
                "            include PersonalBankingCustomer \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        container InternetBankingSystem \"Containers\" {\n" +
                "            include PersonalBankingCustomer \n" +
                "            include InternetBankingSystem.WebApplication \n" +
                "            include InternetBankingSystem.APIApplication \n" +
                "            include InternetBankingSystem.Database \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem.SinglePageApplication \n" +
                "            include InternetBankingSystem.MobileApp \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        component InternetBankingSystem.APIApplication \"Components\" {\n" +
                "            include InternetBankingSystem.APIApplication.SignInController \n" +
                "            include InternetBankingSystem.APIApplication.AccountsSummaryController \n" +
                "            include InternetBankingSystem.APIApplication.ResetPasswordController \n" +
                "            include InternetBankingSystem.APIApplication.SecurityComponent \n" +
                "            include InternetBankingSystem.APIApplication.MainframeBankingSystemFacade \n" +
                "            include InternetBankingSystem.APIApplication.EmailComponent \n" +
                "            include InternetBankingSystem.Database \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem.SinglePageApplication \n" +
                "            include InternetBankingSystem.MobileApp \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        dynamic InternetBankingSystem.APIApplication \"SignIn\" \"Summarises how the sign in feature works in the single-page application.\" {\n" +
                "            # 1 \n" +
                "            InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.SignInController \"Submits credentials to\" \n" +
                "            # 2 \n" +
                "            InternetBankingSystem.APIApplication.SignInController -> InternetBankingSystem.APIApplication.SecurityComponent \"Validates credentials using\" \n" +
                "            # 3 \n" +
                "            InternetBankingSystem.APIApplication.SecurityComponent -> InternetBankingSystem.Database \"select * from users where username = ?\" \n" +
                "            # 4 \n" +
                "            InternetBankingSystem.Database -> InternetBankingSystem.APIApplication.SecurityComponent \"Returns user data to\" \n" +
                "            # 5 \n" +
                "            InternetBankingSystem.APIApplication.SecurityComponent -> InternetBankingSystem.APIApplication.SignInController \"Returns true if the hashed password matches\" \n" +
                "            # 6 \n" +
                "            InternetBankingSystem.APIApplication.SignInController -> InternetBankingSystem.SinglePageApplication \"Sends back an authentication token to\" \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        deployment InternetBankingSystem \"Development\" \"DevelopmentDeployment\" {\n" +
                "            include Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat \n" +
                "            include Development.DeveloperLaptop.DockerContainerDatabaseServer.DatabaseServer \n" +
                "            include Development.DeveloperLaptop.WebBrowser \n" +
                "            include Development.BigBankplc.bigbankdev001 \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        deployment InternetBankingSystem \"Live\" \"LiveDeployment\" {\n" +
                "            include Live.BigBankplc.bigbankdb02.OracleSecondary \n" +
                "            include Live.Customersmobiledevice \n" +
                "            include Live.BigBankplc.bigbankapi.ApacheTomcat \n" +
                "            include Live.BigBankplc.bigbankprod001 \n" +
                "            include Live.Customerscomputer.WebBrowser \n" +
                "            include Live.BigBankplc.bigbankdb01.OraclePrimary \n" +
                "            include Live.BigBankplc.bigbankweb.ApacheTomcat \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Bank Staff\" {\n" +
                "                background \"#999999\" \n" +
                "            }\n" +
                "            element \"Component\" {\n" +
                "                background \"#85bbf0\" \n" +
                "                color \"#000000\" \n" +
                "            }\n" +
                "            element \"Container\" {\n" +
                "                background \"#438dd5\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Customer\" {\n" +
                "                background \"#08427b\" \n" +
                "            }\n" +
                "            element \"Database\" {\n" +
                "                shape \"Cylinder\" \n" +
                "            }\n" +
                "            element \"Existing System\" {\n" +
                "                background \"#999999\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Failover\" {\n" +
                "                opacity \"25\" \n" +
                "            }\n" +
                "            element \"Mobile App\" {\n" +
                "                shape \"MobileDeviceLandscape\" \n" +
                "            }\n" +
                "            element \"Person\" {\n" +
                "                shape \"Person\" \n" +
                "                color \"#ffffff\" \n" +
                "                fontSize \"22\" \n" +
                "            }\n" +
                "            element \"Software System\" {\n" +
                "                background \"#1168bd\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Web Browser\" {\n" +
                "                shape \"WebBrowser\" \n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_frs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("examples/financial-risk-system.dsl"));
        Workspace workspace = parser.getWorkspace();

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Financial Risk System\" \"This is a simple (incomplete) example C4 model based upon the financial risk system architecture kata, which can be found at http://bit.ly/sa4d-risksystem\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        BusinessUser = person \"Business User\" \"A regular business user.\" \n" +
                "        ConfigurationUser = person \"Configuration User\" \"A regular business user who can also configure the parameters used in the risk calculations.\" \n" +
                "        FinancialRiskSystem = softwareSystem \"Financial Risk System\" \"Calculates the bank's exposure to risk for product X.\" \"Financial Risk System\" \n" +
                "        TradeDataSystem = softwareSystem \"Trade Data System\" \"The system of record for trades of type X.\" \n" +
                "        ReferenceDataSystem = softwareSystem \"Reference Data System\" \"Manages reference data for all counterparties the bank interacts with.\" \n" +
                "        ReferenceDataSystemv20 = softwareSystem \"Reference Data System v2.0\" \"Manages reference data for all counterparties the bank interacts with.\" \"Future State\" \n" +
                "        Emailsystem = softwareSystem \"E-mail system\" \"The bank's Microsoft Exchange system.\" \n" +
                "        CentralMonitoringService = softwareSystem \"Central Monitoring Service\" \"The bank's central monitoring and alerting dashboard.\" \n" +
                "        ActiveDirectory = softwareSystem \"Active Directory\" \"The bank's authentication and authorisation system.\" \n" +
                "        BusinessUser -> FinancialRiskSystem \"Views reports using\" \n" +
                "        FinancialRiskSystem -> TradeDataSystem \"Gets trade data from\" \n" +
                "        FinancialRiskSystem -> ReferenceDataSystem \"Gets counterparty data from\" \n" +
                "        FinancialRiskSystem -> ReferenceDataSystemv20 \"Gets counterparty data from\" \"\" \"Future State\" \n" +
                "        ConfigurationUser -> FinancialRiskSystem \"Configures parameters using\" \n" +
                "        FinancialRiskSystem -> Emailsystem \"Sends a notification that a report is ready to\" \n" +
                "        Emailsystem -> BusinessUser \"Sends a notification that a report is ready to\" \"E-mail message\" \"Asynchronous\" \n" +
                "        FinancialRiskSystem -> CentralMonitoringService \"Sends critical failure alerts to\" \"SNMP\" \"Asynchronous,Alert\" \n" +
                "        FinancialRiskSystem -> ActiveDirectory \"Uses for user authentication and authorisation\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext FinancialRiskSystem \"Context\" \"An example System Context diagram for the Financial Risk System architecture kata.\" {\n" +
                "            include BusinessUser \n" +
                "            include ConfigurationUser \n" +
                "            include FinancialRiskSystem \n" +
                "            include TradeDataSystem \n" +
                "            include ReferenceDataSystem \n" +
                "            include ReferenceDataSystemv20 \n" +
                "            include Emailsystem \n" +
                "            include CentralMonitoringService \n" +
                "            include ActiveDirectory \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Element\" {\n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Financial Risk System\" {\n" +
                "                background \"#550000\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Future State\" {\n" +
                "                opacity \"30\" \n" +
                "            }\n" +
                "            element \"Person\" {\n" +
                "                shape \"Person\" \n" +
                "                background \"#d46a6a\" \n" +
                "            }\n" +
                "            element \"Software System\" {\n" +
                "                shape \"RoundedBox\" \n" +
                "                background \"#801515\" \n" +
                "            }\n" +
                "            relationship \"Alert\" {\n" +
                "                color \"#ff0000\" \n" +
                "            }\n" +
                "            relationship \"Asynchronous\" {\n" +
                "                dashed \"true\" \n" +
                "            }\n" +
                "            relationship \"Future State\" {\n" +
                "                opacity \"30\" \n" +
                "            }\n" +
                "            relationship \"Relationship\" {\n" +
                "                dashed \"false\" \n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_escapingQuotes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("Hello \"World\"");

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        assertEquals("workspace \"Name\" \"Description\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        HelloWorld = person \"Hello \\\"World\\\"\" \n" +
                "    }\n" +
                "\n" +
                "}\n", formatter.format(WorkspaceUtils.toJson(workspace, false)));
    }

    @Test
    void test_formattingInternalSoftwareSystemsWhenNoEnterpriseSpecified() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem ss = workspace.getModel().addSoftwareSystem("Software System");
        ss.setLocation(Location.Internal);

        StructurizrDslFormatter formatter = new StructurizrDslFormatter();
        String result = formatter.format(WorkspaceUtils.toJson(workspace, false));

        assertEquals("workspace \"Name\" \"Description\" {\n" +
                "\n" +
                "    !impliedRelationships \"false\" \n" +
                "    !identifiers \"hierarchical\" \n" +
                "\n" +
                "    model {\n" +
                "        enterprise \"Enterprise\" {\n" +
                "            SoftwareSystem = softwareSystem \"Software System\" \n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n", result);
    }


}