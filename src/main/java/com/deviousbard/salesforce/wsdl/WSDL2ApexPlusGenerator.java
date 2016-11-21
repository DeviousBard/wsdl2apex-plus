package com.deviousbard.salesforce.wsdl;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileWriter;

public class WSDL2ApexPlusGenerator {

    private VelocityEngine ve = new VelocityEngine();

    private Template apexSchemaTemplate;
    private Template apexWebServiceTemplate;

    private void generateApexClasses() {
        initializeTemplateEngine();
        parseWSDL();
    }

    private void applyApexSchemaTemplate(SchemaDefinition sd) {
        VelocityContext context = new VelocityContext();
        context.put("schema", sd);
        FileWriter fw = null;
        try {
            fw = new FileWriter("E:/tmp/apex-schema-class.cls");
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

    private void parseWSDL() {
        SchemaDefinition sd = new SchemaDefinition();
        sd.setName("RatingSystemV10_XSD");
        TypeDefinition td = new TypeDefinition();
        td.setName("AgeBandRateCountsType");
        td.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        sd.addType(td);
        ElementDefinition ed = new ElementDefinition();
        ed.setName("ageBandRateCount");
        ed.setType("AgeBandRateCountType");
        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        ed.setRequired("true");
        td.addElement(ed);
        td = new TypeDefinition();
        td.setName("AgeBandRateCountType");
        td.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        sd.addType(td);
        ed = new ElementDefinition();
        ed.setName("age");
        ed.setRequired("true");
        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        ed.setType("xsd:string");
        ed.setPrimitive(true);
        td.addElement(ed);
        ed = new ElementDefinition();
        ed.setName("count");
        ed.setRequired("true");
        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        ed.setType("xsd:string");
        ed.setPrimitive(true);
        td.addElement(ed);
        ed = new ElementDefinition();
        ed.setName("rate");
        ed.setRequired("true");
        ed.setNamespace("http://capbluecross.com/schema/application/RatingSystem/v1.0");
        ed.setType("xsd:string");
        td.addElement(ed);
        ed.setPrimitive(true);
        applyApexSchemaTemplate(sd);
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
        gen.generateApexClasses();
    }
}
