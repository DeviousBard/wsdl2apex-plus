package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.*;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import groovy.xml.QName;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

public class WSDL2ApexPlusGenerator {

    private VelocityEngine ve = new VelocityEngine();

    private Template apexSchemaTemplate;
    private Template apexWebServiceTemplate;
    private Set<String> parsedSchemaSet = new HashSet<>();

    private void generateApexClasses(String wsdlFile) {
        initializeTemplateEngine();
        parseWSDL(wsdlFile);
    }

    private void applyApexSchemaTemplate(SchemaDefinition sd) {
        VelocityContext context = new VelocityContext();
        context.put("schema", sd);
        FileWriter fw = null;
        try {
            fw = new FileWriter("/tmp/" + sd.getName() + ".cls");
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

    private void parseWSDL(String wsdlFile) {
        WSDLParser wsdlParser = new WSDLParser();
        Definitions defs = wsdlParser.parse(wsdlFile);
        String baseDir = (String) defs.getBaseDir();
        //System.out.println("baseDir: " + baseDir);
        for (Schema schema : defs.getSchemas()) {
            parseSchema(schema, baseDir);
        }
    }

    private void parseSchema(Schema schema, String baseDir) {
        SchemaDefinition sd = new SchemaDefinition();
        String schemaLocation = schema.getSchemaLocation();
        if (schemaLocation != null && !schemaLocation.equals("") && !parsedSchemaSet.contains(schemaLocation)) {
            String schemaFileName = baseDir + schemaLocation;
            System.out.println("schemaFileName: " + schemaFileName);
            parsedSchemaSet.add(schemaFileName);
            String schemaName = schemaLocation.split("\\.xsd")[0] + "_XSD";
            //System.out.println("schemaName:  " + schemaName);
            sd.setName(schemaName);
            sd.setNamespace(schema.getTargetNamespace());
            SchemaParser schemaParser = new SchemaParser();
            Schema parsedSchema = schemaParser.parse(schemaFileName);
            for (Import schemaImport : parsedSchema.getImports()) {
                parseSchema(new SchemaParser().parse(schemaFileName), baseDir);
            }
            for (ComplexType ct : schema.getComplexTypes()) {
                TypeDefinition td = new TypeDefinition();
                td.setName(ct.getName());
                td.setNamespace(ct.getNamespaceUri());
                sd.addType(td);

                for (SchemaComponent sc : ((ModelGroup) ct.getModel()).getParticles()) {
                    if (sc.getClass().getSimpleName().equals("Element")) {
                        Element el = (Element) sc;
                        ElementDefinition ed = new ElementDefinition();
                        ed.setName(el.getName());
                        ed.setElementNamespace(el.getNamespaceUri());
                        QName qualifiedType = el.getType();
                        ed.setType(qualifiedType.getLocalPart());
                        ed.setTypeNamespace(qualifiedType.getNamespaceURI());
                        ed.setMinOccurs(el.getMinOccurs() == null || el.getMinOccurs().equals("") ? 0 : Integer.parseInt(el.getMinOccurs()));
                        ed.setMaxOccurs(el.getMaxOccurs() == null || el.getMaxOccurs().equals("") ? 0 : el.getMaxOccurs().equals("unbounded") ? -1 : Integer.parseInt(el.getMaxOccurs()));
//                        System.out.println("element name: " + el.getName() + " - minOccurs: " + el.getMinOccurs() + "   maxOccurs: " + el.getMaxOccurs());
                        ed.setRequired(ed.getMinOccurs() >= 1);
                        ed.setNilable(el.isNillable());
                        td.addElement(ed);
                    }
                }
            }
            applyApexSchemaTemplate(sd);
        }


//        sd.setName("RatingSystemV10_XSD");
//        TypeDefinition td = new TypeDefinition();
//        td.setName("AgeBandRateCountsType");
//        td.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        sd.addType(td);
//        ElementDefinition ed = new ElementDefinition();
//        ed.setName("ageBandRateCount");
//        ed.setType("AgeBandRateCountType");
//        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        ed.setRequired("true");
//        td.addElement(ed);
//        td = new TypeDefinition();
//        td.setName("AgeBandRateCountType");
//        td.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        sd.addType(td);
//        ed = new ElementDefinition();
//        ed.setName("age");
//        ed.setRequired("true");
//        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        ed.setType("xsd:string");
//        ed.setPrimitive(true);
//        td.addElement(ed);
//        ed = new ElementDefinition();
//        ed.setName("count");
//        ed.setRequired("true");
//        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        ed.setType("xsd:string");
//        ed.setPrimitive(true);
//        td.addElement(ed);
//        ed = new ElementDefinition();
//        ed.setName("rate");
//        ed.setRequired("true");
//        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
//        ed.setType("xsd:string");
//        td.addElement(ed);
//        ed.setPrimitive(true);
//        applyApexSchemaTemplate(sd);
    }

    private void initializeTemplateEngine() {
        new ClasspathResourceLoader();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        apexSchemaTemplate = ve.getTemplate("templates/apex-schema.vm");
        apexWebServiceTemplate = ve.getTemplate("templates/apex-web-service.vm");
    }

    public static void main(String[] args) {
        WSDL2ApexPlusGenerator gen = new WSDL2ApexPlusGenerator();
        gen.generateApexClasses(args[0]);
    }
}
