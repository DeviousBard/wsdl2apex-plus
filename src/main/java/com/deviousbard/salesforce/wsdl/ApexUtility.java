package com.deviousbard.salesforce.wsdl;

import java.util.HashMap;
import java.util.Map;

public class ApexUtility {

    private static Map<String, String> apexClassNameMap = new HashMap<>();
    private static Map<String, WsdlDefinition> wsdlDefinitionMap = new HashMap<>();
    private static Map<String, SchemaDefinition> schemaDefinitionMap = new HashMap<>();

    public static String getApexTypeFromBaseType(String simpleType) {
        String apexClass = null;
        if (simpleType != null) {
            switch (simpleType) {
                case "{http://www.w3.org/2001/XMLSchema}string":
                    apexClass = "String";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}decimal":
                    apexClass = "Decimal";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}double":
                    apexClass = "Decimal";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}int":
                    apexClass = "Integer";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}long":
                    apexClass = "Integer";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}NMTOKEN":
                    apexClass = "String";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}ID":
                    apexClass = "String";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}token":
                    apexClass = "String";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}dateTime":
                    apexClass = "DateTime";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}date":
                    apexClass = "Date";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}time":
                    apexClass = "Time";
                    break;
                default:
                    apexClass = null;
            }
        }
        return apexClass;
    }

    public static void addApexClassName(String namespace, String apexClassName) {
        apexClassNameMap.put(namespace, apexClassName);
    }

    public static void addWsdl(String namespace, WsdlDefinition wsdl) {
        wsdlDefinitionMap.put(namespace, wsdl);
    }

    public static void addSchema(String namespace, SchemaDefinition schema) {
        schemaDefinitionMap.put(namespace, schema);
    }

    public static SchemaDefinition getSchemaDefinition(String namespace) {
        return schemaDefinitionMap.get(namespace);
    }

    public static WsdlDefinition getWsdlDefinition(String namespace) {
        return wsdlDefinitionMap.get(namespace);
    }

    public static String getApexClassFromNamespace(String namespace) {
        return apexClassNameMap.get(namespace);
    }
}
