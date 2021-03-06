package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class WSDL2ApexPlusGenerator {

    private Map<String, Definitions> wsdlMap = new HashMap<>();
    private Map<String, Schema> schemaMap = new HashMap<>();

    private VelocityEngine ve = new VelocityEngine();
    private Template apexSchemaTemplate;
    private Template apexWebServiceTemplate;
    private String clientCertName;
    private boolean wsSecurity;
    private boolean environmentSpecificEndPoint;
    private String timeout;
    private boolean failOnFirstError;

    private void generateApex(String wsdlFile) {
        initializeTemplateEngine();
        parseWsdlList(Collections.singletonList(wsdlFile));
        askForClassNames();
        for (String key : schemaMap.keySet()) {
            System.out.print("Schema:\n" + key + " - " + schemaMap.get(key) + "\n");
            SchemaDefinition sd = new SchemaDefinition(schemaMap.get(key));
            if (wsdlMap.get(key) == null) {
                this.applyApexSchemaTemplate(sd);
            }
            for (SimpleTypeDefinition std : sd.getSimpleTypes().values()) {
                System.out.print("   Simple Type: " + std.toString() + "\n");
            }
            for (ComplexTypeDefinition ctd: sd.getComplexTypes().values()) {
                System.out.print("   Complex Type: " + ctd.toString() + "\n");
            }
            for (ElementDefinition ed: sd.getElements().values()) {
                System.out.print("   Element: " + ed.toString() + "\n");
            }
        }
        for (String key : wsdlMap.keySet()) {
            System.out.print("WSDL Definition:\n" + key + " - " + wsdlMap.get(key) + "\n\n");
            SchemaDefinition sd = new SchemaDefinition(schemaMap.get(key));
            WsdlDefinition wd = new WsdlDefinition(wsdlMap.get(key), clientCertName, wsSecurity, environmentSpecificEndPoint, timeout);
            this.applyApexWebServiceTemplate(sd, wd);
        }
    }

    private void initializeTemplateEngine() {
        new ClasspathResourceLoader();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        apexSchemaTemplate = ve.getTemplate("templates/apex-schema.vm");
        apexWebServiceTemplate = ve.getTemplate("templates/apex-web-service.vm");
    }

    private void applyApexWebServiceTemplate(SchemaDefinition sd, WsdlDefinition wd) {
        VelocityContext context = new VelocityContext();
        context.put("schema", sd);
        context.put("wsdl", wd);
        context.put("ApexUtility", ApexUtility.class);
        System.out.println("wd: " + wd);
        FileWriter fw = null;
        try {
            fw = new FileWriter("/tmp/" + ApexUtility.getApexClassFromNamespace(wd.getNamespace()) + ".cls");
            apexWebServiceTemplate.merge(context, fw);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                // Intentionally ignored
            }
        }
    }

    private void applyApexSchemaTemplate(SchemaDefinition sd) {
        VelocityContext context = new VelocityContext();
        context.put("schema", sd);
        context.put("ApexUtility", ApexUtility.class);
        FileWriter fw = null;
        try {
            fw = new FileWriter("/tmp/" + ApexUtility.getApexClassFromNamespace(sd.getNamespace()) + ".cls");
            apexSchemaTemplate.merge(context, fw);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                // Intentionally ignored
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
                if (className == null || className.trim().equals("")) {
                    className = defaultClassName;
                }
                if (defs.getTargetNamespace() != null && !(defs.getTargetNamespace().equals(""))) {
                    ApexUtility.addApexClassName(defs.getTargetNamespace(), className);
                }
            }

            for (String key : schemaMap.keySet()) {
                if (wsdlMap.get(key) == null) {
                    Schema schema = schemaMap.get(key);
                    String fileName = schema.getSchemaLocation();
                    int index = (fileName.lastIndexOf('.') == -1 ? fileName.length() : fileName.lastIndexOf('.'));
                    String defaultClassName = fileName.substring(fileName.lastIndexOf("/") + 1, index) + "_XSD";
                    System.out.print("Schema Class Name for Target Namespace \"" + schema.getTargetNamespace() + "\" [" + defaultClassName + "]: ");
                    String className = br.readLine();
                    if (className == null || className.trim().equals("")) {
                        className = defaultClassName;
                    }
                    if (schema.getTargetNamespace() != null && !(schema.getTargetNamespace().equals(""))) {
                        ApexUtility.addApexClassName(schema.getTargetNamespace(), className);
                    }
                }
            }
            System.out.print("Client Certificate Name []: ");
            this.clientCertName = br.readLine();

            System.out.print("Use WS-Security (Y/N) [N]: ");
            String wsSecurityAnswer = br.readLine();
            this.wsSecurity = (wsSecurityAnswer != null && wsSecurityAnswer.toLowerCase().equals("y"));

            System.out.print("Use Environment Specific End-Points (Y/N) [N]: ");
            String environmentSpecificEndPointAnswer = br.readLine();
            this.environmentSpecificEndPoint = (environmentSpecificEndPointAnswer != null && environmentSpecificEndPointAnswer.toLowerCase().equals("y"));

            System.out.print("Web Service Timeout (in ms) (1 - 120000) [10000]: ");
            String timeoutAnswer = br.readLine();
            this.timeout = (timeoutAnswer == null || timeoutAnswer.trim().equals("") ? "10000" : "" + Integer.parseInt(timeoutAnswer));

            System.out.print("Fail on first error or accumulate all errors (F or A) [F]: ");
            String failOnFirstErrorAnswer = br.readLine();
            this.failOnFirstError = ((failOnFirstErrorAnswer == null || failOnFirstErrorAnswer.trim().equals("") || failOnFirstErrorAnswer.trim().toLowerCase().equals("f")) || (!failOnFirstErrorAnswer.trim().toLowerCase().equals("a")));

        } catch (Exception e) {
            System.err.print("Exception: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void parseWsdlList(List<String> wsdlFileList) {
        WSDLParser wsdlParser = new WSDLParser();
        for (String wsdlFile : wsdlFileList) {
            Definitions defs = wsdlParser.parse(wsdlFile);
            String baseDir = (String) defs.getBaseDir();
            for (Schema localSchema : defs.getLocalSchemas()) {
                schemaMap.put(localSchema.getTargetNamespace(), localSchema);
            }
            parseSchemaList(defs.getLocalSchemas(), baseDir);
            if (!(wsdlMap.keySet().contains(defs.getTargetNamespace()))) {
                wsdlMap.put(defs.getTargetNamespace(), defs);
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
        WSDL2ApexPlusGenerator generator = new WSDL2ApexPlusGenerator();
        generator.generateApex(args[0]);
    }
}
