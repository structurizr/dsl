package com.structurizr.dsl;

import com.structurizr.Workspace;
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
                "    model {\n" +
                "        impliedRelationships \"false\" \n" +
                "\n" +
                "    }\n" +
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
                "    model {\n" +
                "        impliedRelationships \"false\" \n" +
                "\n" +
                "        User = person \"User\" \"A user of my software system.\" \"\" \n" +
                "        SoftwareSystem = softwareSystem \"Software System\" \"My software system.\" \"\" \n" +
                "        User -> SoftwareSystem \"Uses\" \"\" \"\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemContext SoftwareSystem \"SoftwareSystem-SystemContext\" \"\" {\n" +
                "            include User \n" +
                "            include SoftwareSystem \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        styles {\n" +
                "            element \"Person\" {\n" +
                "                shape \"Person\" \n" +
                "                background \"#08427b\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "            element \"Software System\" {\n" +
                "                background \"#1168bd\" \n" +
                "                color \"#ffffff\" \n" +
                "            }\n" +
                "        }\n" +
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
                "    model {\n" +
                "        impliedRelationships \"false\" \n" +
                "\n" +
                "        SpringPetClinic = softwareSystem \"Spring PetClinic\" \"Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.\" \"Spring Boot Application\" {\n" +
                "            SpringPetClinic_WebApplication = container \"Web Application\" \"Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.\" \"Java and Spring Boot\" \"\" \n" +
                "            SpringPetClinic_Database = container \"Database\" \"Stores information regarding the veterinarians, the clients, and their pets.\" \"Relational database schema\" \"Database\" \n" +
                "        }\n" +
                "        SpringPetClinic_WebApplication -> SpringPetClinic_Database \"Reads from and writes to\" \"JDBC/SSL\" \"\" \n" +
                "\n" +
                "        deploymentEnvironment \"Live\" {\n" +
                "            Live_AmazonWebServices = deploymentNode \"Amazon Web Services\" \"\" \"\" \"Amazon Web Services - Cloud\" {\n" +
                "                Live_AmazonWebServices_USEast1 = deploymentNode \"US-East-1\" \"\" \"\" \"Amazon Web Services - Region\" {\n" +
                "                    Live_AmazonWebServices_USEast1_Route53 = infrastructureNode \"Route 53\" \"\" \"\" \"Amazon Web Services - Route 53\" \n" +
                "                    Live_AmazonWebServices_USEast1_ElasticLoadBalancer = infrastructureNode \"Elastic Load Balancer\" \"\" \"\" \"Amazon Web Services - Elastic Load Balancing\" \n" +
                "                    Live_AmazonWebServices_USEast1_AmazonRDS = deploymentNode \"Amazon RDS\" \"\" \"\" \"Amazon Web Services - RDS\" {\n" +
                "                        Live_AmazonWebServices_USEast1_AmazonRDS_MySQL = deploymentNode \"MySQL\" \"\" \"\" \"Amazon Web Services - RDS_MySQL_instance\" {\n" +
                "                            Live_AmazonWebServices_USEast1_AmazonRDS_MySQL_Database_1 = containerInstance SpringPetClinic_Database \"\" \n" +
                "                        }\n" +
                "                    }\n" +
                "                    Live_AmazonWebServices_USEast1_Autoscalinggroup = deploymentNode \"Autoscaling group\" \"\" \"\" \"Amazon Web Services - Auto Scaling\" {\n" +
                "                        Live_AmazonWebServices_USEast1_Autoscalinggroup_AmazonEC2 = deploymentNode \"Amazon EC2\" \"\" \"\" \"Amazon Web Services - EC2\" {\n" +
                "                            Live_AmazonWebServices_USEast1_Autoscalinggroup_AmazonEC2_WebApplication_1 = containerInstance SpringPetClinic_WebApplication \"\" \n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Live_AmazonWebServices_USEast1_Route53 -> Live_AmazonWebServices_USEast1_ElasticLoadBalancer \"Forwards requests to\" \"HTTPS\" \"\" \n" +
                "        Live_AmazonWebServices_USEast1_ElasticLoadBalancer -> Live_AmazonWebServices_USEast1_Autoscalinggroup_AmazonEC2_WebApplication_1 \"Forwards requests to\" \"HTTPS\" \"\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        deployment SpringPetClinic \"Live\" \"AmazonWebServicesDeployment\" \"\" {\n" +
                "            include Live_AmazonWebServices_USEast1_Autoscalinggroup_AmazonEC2 \n" +
                "            include Live_AmazonWebServices_USEast1_AmazonRDS_MySQL \n" +
                "            include Live_AmazonWebServices_USEast1 \n" +
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
                "    model {\n" +
                "        impliedRelationships \"false\" \n" +
                "\n" +
                "        enterprise \"Big Bank plc\" {\n" +
                "            CustomerServiceStaff = person \"Customer Service Staff\" \"Customer service staff within the bank.\" \"Bank Staff\" \n" +
                "            BackOfficeStaff = person \"Back Office Staff\" \"Administration and support staff within the bank.\" \"Bank Staff\" \n" +
                "            MainframeBankingSystem = softwareSystem \"Mainframe Banking System\" \"Stores all of the core banking information about customers, accounts, transactions, etc.\" \"Existing System\" \n" +
                "            EmailSystem = softwareSystem \"E-mail System\" \"The internal Microsoft Exchange e-mail system.\" \"Existing System\" \n" +
                "            ATM = softwareSystem \"ATM\" \"Allows customers to withdraw cash.\" \"Existing System\" \n" +
                "            InternetBankingSystem = softwareSystem \"Internet Banking System\" \"Allows customers to view information about their bank accounts, and make payments.\" \"\" {\n" +
                "                InternetBankingSystem_WebApplication = container \"Web Application\" \"Delivers the static content and the Internet banking single page application.\" \"Java and Spring MVC\" \"\" \n" +
                "                InternetBankingSystem_APIApplication = container \"API Application\" \"Provides Internet banking functionality via a JSON/HTTPS API.\" \"Java and Spring MVC\" \"\" {\n" +
                "                    InternetBankingSystem_APIApplication_SignInController = component \"Sign In Controller\" \"Allows users to sign in to the Internet Banking System.\" \"Spring MVC Rest Controller\" \"\" \n" +
                "                    InternetBankingSystem_APIApplication_AccountsSummaryController = component \"Accounts Summary Controller\" \"Provides customers with a summary of their bank accounts.\" \"Spring MVC Rest Controller\" \"\" \n" +
                "                    InternetBankingSystem_APIApplication_ResetPasswordController = component \"Reset Password Controller\" \"Allows users to reset their passwords with a single use URL.\" \"Spring MVC Rest Controller\" \"\" \n" +
                "                    InternetBankingSystem_APIApplication_SecurityComponent = component \"Security Component\" \"Provides functionality related to signing in, changing passwords, etc.\" \"Spring Bean\" \"\" \n" +
                "                    InternetBankingSystem_APIApplication_MainframeBankingSystemFacade = component \"Mainframe Banking System Facade\" \"A facade onto the mainframe banking system.\" \"Spring Bean\" \"\" \n" +
                "                    InternetBankingSystem_APIApplication_EmailComponent = component \"E-mail Component\" \"Sends e-mails to users.\" \"Spring Bean\" \"\" \n" +
                "                }\n" +
                "                InternetBankingSystem_Database = container \"Database\" \"Stores user registration information, hashed authentication credentials, access logs, etc.\" \"Oracle Database Schema\" \"Database\" \n" +
                "                InternetBankingSystem_SinglePageApplication = container \"Single-Page Application\" \"Provides all of the Internet banking functionality to customers via their web browser.\" \"JavaScript and Angular\" \"Web Browser\" \n" +
                "                InternetBankingSystem_MobileApp = container \"Mobile App\" \"Provides a limited subset of the Internet banking functionality to customers via their mobile device.\" \"Xamarin\" \"Mobile App\" \n" +
                "            }\n" +
                "        }\n" +
                "        PersonalBankingCustomer = person \"Personal Banking Customer\" \"A customer of the bank, with personal bank accounts.\" \"Customer\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem \"Views account balances, and makes payments using\" \"\" \"\" \n" +
                "        InternetBankingSystem -> MainframeBankingSystem \"Gets account information from, and makes payments using\" \"\" \"\" \n" +
                "        InternetBankingSystem -> EmailSystem \"Sends e-mail using\" \"\" \"\" \n" +
                "        EmailSystem -> PersonalBankingCustomer \"Sends e-mails to\" \"\" \"\" \n" +
                "        PersonalBankingCustomer -> CustomerServiceStaff \"Asks questions to\" \"Telephone\" \"\" \n" +
                "        CustomerServiceStaff -> MainframeBankingSystem \"Uses\" \"\" \"\" \n" +
                "        PersonalBankingCustomer -> ATM \"Withdraws cash using\" \"\" \"\" \n" +
                "        ATM -> MainframeBankingSystem \"Uses\" \"\" \"\" \n" +
                "        BackOfficeStaff -> MainframeBankingSystem \"Uses\" \"\" \"\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem_WebApplication \"Visits bigbank.com/ib using\" \"HTTPS\" \"\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem_SinglePageApplication \"Views account balances, and makes payments using\" \"\" \"\" \n" +
                "        PersonalBankingCustomer -> InternetBankingSystem_MobileApp \"Views account balances, and makes payments using\" \"\" \"\" \n" +
                "        InternetBankingSystem_WebApplication -> InternetBankingSystem_SinglePageApplication \"Delivers to the customer's web browser\" \"\" \"\" \n" +
                "        InternetBankingSystem_SinglePageApplication -> InternetBankingSystem_APIApplication_SignInController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_SinglePageApplication -> InternetBankingSystem_APIApplication \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_SinglePageApplication -> InternetBankingSystem_APIApplication_AccountsSummaryController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_SinglePageApplication -> InternetBankingSystem_APIApplication_ResetPasswordController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_MobileApp -> InternetBankingSystem_APIApplication_SignInController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_MobileApp -> InternetBankingSystem_APIApplication \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_MobileApp -> InternetBankingSystem_APIApplication_AccountsSummaryController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_MobileApp -> InternetBankingSystem_APIApplication_ResetPasswordController \"Makes API calls to\" \"JSON/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_SignInController -> InternetBankingSystem_APIApplication_SecurityComponent \"Uses\" \"\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_AccountsSummaryController -> InternetBankingSystem_APIApplication_MainframeBankingSystemFacade \"Uses\" \"\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_ResetPasswordController -> InternetBankingSystem_APIApplication_SecurityComponent \"Uses\" \"\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_ResetPasswordController -> InternetBankingSystem_APIApplication_EmailComponent \"Uses\" \"\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_SecurityComponent -> InternetBankingSystem_Database \"Reads from and writes to\" \"JDBC\" \"\" \n" +
                "        InternetBankingSystem_APIApplication -> InternetBankingSystem_Database \"Reads from and writes to\" \"JDBC\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_MainframeBankingSystemFacade -> MainframeBankingSystem \"Makes API calls to\" \"XML/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_APIApplication -> MainframeBankingSystem \"Makes API calls to\" \"XML/HTTPS\" \"\" \n" +
                "        InternetBankingSystem_APIApplication_EmailComponent -> EmailSystem \"Sends e-mail using\" \"\" \"\" \n" +
                "        InternetBankingSystem_APIApplication -> EmailSystem \"Sends e-mail using\" \"\" \"\" \n" +
                "\n" +
                "        deploymentEnvironment \"Development\" {\n" +
                "            Development_DeveloperLaptop = deploymentNode \"Developer Laptop\" \"\" \"Microsoft Windows 10 or Apple macOS\" \"\" {\n" +
                "                Development_DeveloperLaptop_WebBrowser = deploymentNode \"Web Browser\" \"\" \"Chrome, Firefox, Safari, or Edge\" \"\" {\n" +
                "                    Development_DeveloperLaptop_WebBrowser_SinglePageApplication_1 = containerInstance InternetBankingSystem_SinglePageApplication \"\" \n" +
                "                }\n" +
                "                Development_DeveloperLaptop_DockerContainerWebServer = deploymentNode \"Docker Container - Web Server\" \"\" \"Docker\" \"\" {\n" +
                "                    Development_DeveloperLaptop_DockerContainerWebServer_ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" \"\" {\n" +
                "                        Development_DeveloperLaptop_DockerContainerWebServer_ApacheTomcat_WebApplication_1 = containerInstance InternetBankingSystem_WebApplication \"\" \n" +
                "                        Development_DeveloperLaptop_DockerContainerWebServer_ApacheTomcat_APIApplication_1 = containerInstance InternetBankingSystem_APIApplication \"\" \n" +
                "                    }\n" +
                "                }\n" +
                "                Development_DeveloperLaptop_DockerContainerDatabaseServer = deploymentNode \"Docker Container - Database Server\" \"\" \"Docker\" \"\" {\n" +
                "                    Development_DeveloperLaptop_DockerContainerDatabaseServer_DatabaseServer = deploymentNode \"Database Server\" \"\" \"Oracle 12c\" \"\" {\n" +
                "                        Development_DeveloperLaptop_DockerContainerDatabaseServer_DatabaseServer_Database_1 = containerInstance InternetBankingSystem_Database \"\" \n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "            Development_BigBankplc = deploymentNode \"Big Bank plc\" \"\" \"Big Bank plc data center\" \"\" {\n" +
                "                Development_BigBankplc_bigbankdev001 = deploymentNode \"bigbank-dev001\" \"\" \"\" \"\" {\n" +
                "                    Development_BigBankplc_bigbankdev001_MainframeBankingSystem_1 = softwareSystemInstance MainframeBankingSystem \"\" \n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        deploymentEnvironment \"Live\" {\n" +
                "            Live_Customersmobiledevice = deploymentNode \"Customer's mobile device\" \"\" \"Apple iOS or Android\" \"\" {\n" +
                "                Live_Customersmobiledevice_MobileApp_1 = containerInstance InternetBankingSystem_MobileApp \"\" \n" +
                "            }\n" +
                "            Live_Customerscomputer = deploymentNode \"Customer's computer\" \"\" \"Microsoft Windows or Apple macOS\" \"\" {\n" +
                "                Live_Customerscomputer_WebBrowser = deploymentNode \"Web Browser\" \"\" \"Chrome, Firefox, Safari, or Edge\" \"\" {\n" +
                "                    Live_Customerscomputer_WebBrowser_SinglePageApplication_1 = containerInstance InternetBankingSystem_SinglePageApplication \"\" \n" +
                "                }\n" +
                "            }\n" +
                "            Live_BigBankplc = deploymentNode \"Big Bank plc\" \"\" \"Big Bank plc data center\" \"\" {\n" +
                "                Live_BigBankplc_bigbankweb = deploymentNode \"bigbank-web***\" \"\" \"Ubuntu 16.04 LTS\" \"\" {\n" +
                "                    Live_BigBankplc_bigbankweb_ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" \"\" {\n" +
                "                        Live_BigBankplc_bigbankweb_ApacheTomcat_WebApplication_1 = containerInstance InternetBankingSystem_WebApplication \"\" \n" +
                "                    }\n" +
                "                }\n" +
                "                Live_BigBankplc_bigbankapi = deploymentNode \"bigbank-api***\" \"\" \"Ubuntu 16.04 LTS\" \"\" {\n" +
                "                    Live_BigBankplc_bigbankapi_ApacheTomcat = deploymentNode \"Apache Tomcat\" \"\" \"Apache Tomcat 8.x\" \"\" {\n" +
                "                        Live_BigBankplc_bigbankapi_ApacheTomcat_APIApplication_1 = containerInstance InternetBankingSystem_APIApplication \"\" \n" +
                "                    }\n" +
                "                }\n" +
                "                Live_BigBankplc_bigbankdb01 = deploymentNode \"bigbank-db01\" \"\" \"Ubuntu 16.04 LTS\" \"\" {\n" +
                "                    Live_BigBankplc_bigbankdb01_OraclePrimary = deploymentNode \"Oracle - Primary\" \"\" \"Oracle 12c\" \"\" {\n" +
                "                        Live_BigBankplc_bigbankdb01_OraclePrimary_Database_1 = containerInstance InternetBankingSystem_Database \"\" \n" +
                "                    }\n" +
                "                }\n" +
                "                Live_BigBankplc_bigbankdb02 = deploymentNode \"bigbank-db02\" \"\" \"Ubuntu 16.04 LTS\" \"Failover\" {\n" +
                "                    Live_BigBankplc_bigbankdb02_OracleSecondary = deploymentNode \"Oracle - Secondary\" \"\" \"Oracle 12c\" \"Failover\" {\n" +
                "                        Live_BigBankplc_bigbankdb02_OracleSecondary_Database_1 = containerInstance InternetBankingSystem_Database \"Failover\" \n" +
                "                    }\n" +
                "                }\n" +
                "                Live_BigBankplc_bigbankprod001 = deploymentNode \"bigbank-prod001\" \"\" \"\" \"\" {\n" +
                "                    Live_BigBankplc_bigbankprod001_MainframeBankingSystem_1 = softwareSystemInstance MainframeBankingSystem \"\" \n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Live_BigBankplc_bigbankdb01_OraclePrimary -> Live_BigBankplc_bigbankdb02_OracleSecondary \"Replicates data to\" \"\" \"\" \n" +
                "    }\n" +
                "\n" +
                "    views {\n" +
                "        systemLandscape \"SystemLandscape\" \"\" {\n" +
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
                "        systemContext InternetBankingSystem \"SystemContext\" \"\" {\n" +
                "            include PersonalBankingCustomer \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        container InternetBankingSystem \"Containers\" \"\" {\n" +
                "            include PersonalBankingCustomer \n" +
                "            include InternetBankingSystem_WebApplication \n" +
                "            include InternetBankingSystem_APIApplication \n" +
                "            include InternetBankingSystem_Database \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem_SinglePageApplication \n" +
                "            include InternetBankingSystem_MobileApp \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        component InternetBankingSystem_APIApplication \"Components\" \"\" {\n" +
                "            include InternetBankingSystem_APIApplication_SignInController \n" +
                "            include InternetBankingSystem_APIApplication_AccountsSummaryController \n" +
                "            include InternetBankingSystem_APIApplication_ResetPasswordController \n" +
                "            include InternetBankingSystem_APIApplication_SecurityComponent \n" +
                "            include InternetBankingSystem_APIApplication_MainframeBankingSystemFacade \n" +
                "            include InternetBankingSystem_APIApplication_EmailComponent \n" +
                "            include InternetBankingSystem_Database \n" +
                "            include MainframeBankingSystem \n" +
                "            include EmailSystem \n" +
                "            include InternetBankingSystem_SinglePageApplication \n" +
                "            include InternetBankingSystem_MobileApp \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        dynamic InternetBankingSystem_APIApplication {\n" +
                "            # 1 \n" +
                "            InternetBankingSystem_SinglePageApplication -> InternetBankingSystem_APIApplication_SignInController \"Submits credentials to\" \n" +
                "            # 2 \n" +
                "            InternetBankingSystem_APIApplication_SignInController -> InternetBankingSystem_APIApplication_SecurityComponent \"Validates credentials using\" \n" +
                "            # 3 \n" +
                "            InternetBankingSystem_APIApplication_SecurityComponent -> InternetBankingSystem_Database \"select * from users where username = ?\" \n" +
                "            # 4 \n" +
                "            InternetBankingSystem_Database -> InternetBankingSystem_APIApplication_SecurityComponent \"Returns user data to\" \n" +
                "            # 5 \n" +
                "            InternetBankingSystem_APIApplication_SecurityComponent -> InternetBankingSystem_APIApplication_SignInController \"Returns true if the hashed password matches\" \n" +
                "            # 6 \n" +
                "            InternetBankingSystem_APIApplication_SignInController -> InternetBankingSystem_SinglePageApplication \"Sends back an authentication token to\" \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        deployment InternetBankingSystem \"Development\" \"DevelopmentDeployment\" \"\" {\n" +
                "            include Development_DeveloperLaptop_DockerContainerWebServer_ApacheTomcat \n" +
                "            include Development_DeveloperLaptop_DockerContainerDatabaseServer_DatabaseServer \n" +
                "            include Development_DeveloperLaptop_WebBrowser \n" +
                "            include Development_BigBankplc_bigbankdev001 \n" +
                "            autolayout tb 300 300 \n" +
                "        }\n" +
                "\n" +
                "        deployment InternetBankingSystem \"Live\" \"LiveDeployment\" \"\" {\n" +
                "            include Live_BigBankplc_bigbankdb02_OracleSecondary \n" +
                "            include Live_Customersmobiledevice \n" +
                "            include Live_BigBankplc_bigbankapi_ApacheTomcat \n" +
                "            include Live_BigBankplc_bigbankprod001 \n" +
                "            include Live_Customerscomputer_WebBrowser \n" +
                "            include Live_BigBankplc_bigbankdb01_OraclePrimary \n" +
                "            include Live_BigBankplc_bigbankweb_ApacheTomcat \n" +
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
                "    model {\n" +
                "        impliedRelationships \"false\" \n" +
                "\n" +
                "        BusinessUser = person \"Business User\" \"A regular business user.\" \"\" \n" +
                "        ConfigurationUser = person \"Configuration User\" \"A regular business user who can also configure the parameters used in the risk calculations.\" \"\" \n" +
                "        FinancialRiskSystem = softwareSystem \"Financial Risk System\" \"Calculates the bank's exposure to risk for product X.\" \"Financial Risk System\" \n" +
                "        TradeDataSystem = softwareSystem \"Trade Data System\" \"The system of record for trades of type X.\" \"\" \n" +
                "        ReferenceDataSystem = softwareSystem \"Reference Data System\" \"Manages reference data for all counterparties the bank interacts with.\" \"\" \n" +
                "        ReferenceDataSystemv20 = softwareSystem \"Reference Data System v2.0\" \"Manages reference data for all counterparties the bank interacts with.\" \"Future State\" \n" +
                "        Emailsystem = softwareSystem \"E-mail system\" \"The bank's Microsoft Exchange system.\" \"\" \n" +
                "        CentralMonitoringService = softwareSystem \"Central Monitoring Service\" \"The bank's central monitoring and alerting dashboard.\" \"\" \n" +
                "        ActiveDirectory = softwareSystem \"Active Directory\" \"The bank's authentication and authorisation system.\" \"\" \n" +
                "        BusinessUser -> FinancialRiskSystem \"Views reports using\" \"\" \"\" \n" +
                "        FinancialRiskSystem -> TradeDataSystem \"Gets trade data from\" \"\" \"\" \n" +
                "        FinancialRiskSystem -> ReferenceDataSystem \"Gets counterparty data from\" \"\" \"\" \n" +
                "        FinancialRiskSystem -> ReferenceDataSystemv20 \"Gets counterparty data from\" \"\" \"Future State\" \n" +
                "        ConfigurationUser -> FinancialRiskSystem \"Configures parameters using\" \"\" \"\" \n" +
                "        FinancialRiskSystem -> Emailsystem \"Sends a notification that a report is ready to\" \"\" \"\" \n" +
                "        Emailsystem -> BusinessUser \"Sends a notification that a report is ready to\" \"E-mail message\" \"Asynchronous\" \n" +
                "        FinancialRiskSystem -> CentralMonitoringService \"Sends critical failure alerts to\" \"SNMP\" \"Asynchronous,Alert\" \n" +
                "        FinancialRiskSystem -> ActiveDirectory \"Uses for user authentication and authorisation\" \"\" \"\" \n" +
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

}