package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;

import java.util.*;

public class WSDL2ApexPlusGenerator2 {

    Map<String, Definitions> wsdlMap = new HashMap<>();
    Map<String, Schema> schemaMap = new HashMap<>();

    private void generateApex(String wsdlFile) {
        String[] wsdlFileList = new String[]{wsdlFile};
        parseWsdlList(Arrays.asList(wsdlFileList));
        for (String key : wsdlMap.keySet()) {
            System.out.println("WSDL Definition: " + key + " - " + wsdlMap.get(key));
        }
        for (String key : schemaMap.keySet()) {
            System.out.println("Schema: " + key + " - " + schemaMap.get(key));
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
