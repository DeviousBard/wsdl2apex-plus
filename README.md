# wsdl2apex-plus
An enhanced version of the Salesforce WSDL2Apex tool that generates Apex class files from WSDL and schemas files.

At my company, from Salesforce, we needed to call internally created web services to talk to our backend systems.  [Salesforce's WSDL2Apex] (https://github.com/forcedotcom/WSDL2Apex) tool would NOT generate the Apex code at all without extensive editing/combining of the WSDL and schema files into one document, and even then it was hit or miss as to whether it would even work. 

We were able to use the [FuseIT](http://www.fuseit.com/products/fuseit-sfdc-explorer/) tool, and it would consistently generate the Apex code without requiring the WSDL and schema files to be changed, but even it wasn't perfect, and we still needed to make a lot of changes to the generated code to make it do what we wanted it to do.

This is my attempt at a version of WSDL2Apex that is a bit more full-featured.  My goal was to add all of the functionality that we needed at my company so that there was ZERO editing required of either the WSDL/schema files or generated code.  

Here are some additional features that I added:

- Handles imported WSDL and schema files.
- Optionally builds WS-Security header for UsernameToken and Timestamp with expiry elements -- requires the included Apex class file "WS_Security.cls", and a Custom Metadata Type for the username and password.
- Optionally has the ability to specify differing end-points by environment (e.g. TEST, ACCEPTANCE, PRODUCTION) using Custom Metadata Types. 
- Optionally accepts the name of a client certificate.
- Optionally allows for a timeout value to be set.
- Generates code that validates against ALL schema simple type restrictions (e.g. minimum length, maximum length, fixed length, pattern matching, and enumerated values), and throws an exception when an attempt is made to set an invalid value into a field.
- Generates code to make sure that all required elements are populated in a complex type before the web service is executed, and throws an exception, if they are missing.

__NOTE/CAVEAT: This implementation was created specifically for a project at my company.    We have a standard design for our WSDL and schema files making them relatively consistent.  The generated code works well on files that follow our standard.  I can't guarantee that it's generic enough to handle ALL of the possible schema and WSDL combinations.  So, it's provided as-is.__
