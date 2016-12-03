package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class WSDL2ApexPlusGenerator2 {

    Map<String, Definitions> wsdlMap = new HashMap<>();
    Map<String, Schema> schemaMap = new HashMap<>();
    Map<String, String> classNameMap = new HashMap<>();

    private void generateApex(String wsdlFile) {
        parseWsdlList(Collections.singletonList(wsdlFile));
        askForClassNames();
        for (String key : wsdlMap.keySet()) {
            System.out.print("WSDL Definition:\n" + key + " - " + wsdlMap.get(key) + "\n\n");
        }

        for (String key : schemaMap.keySet()) {
            System.out.print("Schema:\n" + key + " - " + schemaMap.get(key) + "\n");
            SchemaDefinition sd = new SchemaDefinition(schemaMap.get(key), classNameMap);
            for (SimpleTypeDefinition std : sd.getSimpleTypes().values()) {
                System.out.print("   Simple Type: " + std.toString() + "\n");
            }
        }
    }

    private void askForClassNames() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (String key : wsdlMap.keySet()) {
                Definitions defs = wsdlMap.get(key);
                String fileName = defs.getName();
                String defaultClassName = fileName.replace('.', '_') + "_WS";
                System.out.print("Web Service Class Name for Target Namespace \"" + defs.getTargetNamespace() + "\" [" + defaultClassName + "]: ");
                String className = br.readLine();
                if (className == null || className.equals("")) {
                    className = defaultClassName;
                }
                if (defs.getTargetNamespace() != null && !(defs.getTargetNamespace().equals(""))) {
                    classNameMap.put(defs.getTargetNamespace(), className);
                }
            }

            for (String key : schemaMap.keySet()) {
                Schema schema = schemaMap.get(key);
                String fileName = schema.getSchemaLocation();
                int index = (fileName.lastIndexOf('.') == -1 ? fileName.length() : fileName.lastIndexOf('.'));
                String defaultClassName = fileName.substring(fileName.lastIndexOf("/") + 1, index) + "_XSD";
                System.out.print("Schema Class Name for Target Namespace \"" + schema.getTargetNamespace() + "\" [" + defaultClassName + "]: ");
                String className = br.readLine();
                if (className == null || className.equals("")) {
                    className = defaultClassName;
                }
                if (schema.getTargetNamespace() != null && !(schema.getTargetNamespace().equals(""))) {
                    classNameMap.put(schema.getTargetNamespace(), className);
                }
            }
        } catch (Exception e) {
            System.err.print("Exception: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void parseWsdlList(List<String> wsdlFileList) {
        WSDLParser wsdlParser = new WSDLParser();
        for (String wsdlFile : wsdlFileList) {
            Definitions defs = wsdlParser.parse(wsdlFile);
            if (!(wsdlMap.keySet().contains(defs.getTargetNamespace()))) {
                wsdlMap.put(defs.getTargetNamespace(), defs);
                String baseDir = (String) defs.getBaseDir();
                parseSchemaList(defs.getSchemas(), baseDir);
                if (defs.getImports() != null) {
                    List<String> importWsdlFileList = new ArrayList<>();
                    for (com.predic8.wsdl.Import wsdlRef : defs.getImports()) {
                        importWsdlFileList.add(baseDir + wsdlRef.getLocation());
                        parseWsdlList(importWsdlFileList);
                    }
                }
            }
        }
    }

    private void parseSchemaList(List<Schema> schemas, String baseDir) {
        for (Schema schemaRef : schemas) {
            SchemaParser schemaParser = new SchemaParser();
            if (schemaRef.getSchemaLocation() != null && !(schemaRef.getSchemaLocation().equals("")) && !(schemaMap.keySet().contains(schemaRef.getTargetNamespace()))) {
                Schema schema = schemaParser.parse(baseDir + schemaRef.getSchemaLocation());
                schemaMap.put(schemaRef.getTargetNamespace(), schema);
                if (schema.getImportedSchemas() != null) {
                    parseSchemaList(schema.getImportedSchemas(), baseDir);
                }
            }
        }
    }

    public static void main(String[] args) {
        WSDL2ApexPlusGenerator2 generator = new WSDL2ApexPlusGenerator2();
        generator.generateApex(args[0]);
    }
}
