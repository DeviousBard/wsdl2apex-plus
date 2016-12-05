package com.deviousbard.salesforce.wsdl;

import java.util.HashMap;
import java.util.Map;

public class ApexUtility {

    private static Map<String, String> apexClassNameMap = new HashMap<>();

    public static String getApexTypeFromSimpleType(String simpleType) {
        String apexClass = null;
        if (simpleType != null) {
            switch (simpleType) {
                case "{http://www.w3.org/2001/XMLSchema}string":
                    apexClass = "String";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}decimal":
                    apexClass = "Decimal";
                    break;
                case "{http://www.w3.org/2001/XMLSchema}int":
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

    public static String getApexClassFromNamespace(String namespace) {
        return apexClassNameMap.get(namespace);
    }
}
