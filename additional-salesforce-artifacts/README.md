This directory contains additional Salesforce artifiacts that will need to be created to support the following features of the wsdl2apex-plus generator:
 
1. **WS-Security**: Requires that the "WS_Security.cls" Apex file in this directory is loaded into your Salesforce org.
2. **Environment-Specific End-Points**: Requires that a Custom Metadata Type is created with the following properties:
   1. Single Label: **Web Service End-Point**
   2. Plural label: **Web Service End-Points**
   3. Object Name: **WebServiceEndPoint**
   4. API Name: **WebServiceEndPoint__mdt**
   5. Custom Fields:
        1. End-Point URL:
            1. Field Label: **End-Point URL**
            2. Field Name: **End_Point_URL**
            3. API Name: **End_Point_URL__c**
            4. Data Type: **Text**
            5. Length: **255**
            6. Required: **true**
3. **UsernameToken And Timestamp Expiry**:  Requires that a Custom MetadataType is created with the following properties:
   1. Single Label: **Web Service Credentials**
   2. Plural Label: **Web Service Credentials**
   3. Object Name: **WebServiceCredentials**
   4. API Name: **WebServiceCredentials__mdt**
   5. Custom Fields:
        1. User ID:
            1. Field Label: **User ID**
            2. Field Name: **User_ID**
            3. API Name: **User_ID__c**
            4. Data Type: **Text**
            5. Length: **200**
            6. Required: **true**
        2. Password:
            1. Field Label: **Password**
            2. Field Name: **Password**
            3. API Name: **Password__c**
            4. Data Type: **Text**
            5. Length: **200**
            6. Required: **true**
            
The "wsdl2apex-plus" generator assumes a Custom Metadata Type record exists that matches the name of your generated Web Service Apex class file.  So, you will need to make sure that you create a new record in the "Web Service End-Point" and "Web Service Credentials" Custom Metadata Types with the name that you specified for your Web service Apex class (e.g. "MyWebService_WS").   