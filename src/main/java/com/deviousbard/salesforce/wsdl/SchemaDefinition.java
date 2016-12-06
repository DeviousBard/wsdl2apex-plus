package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SimpleType;

import java.util.HashMap;
import java.util.Map;

public class SchemaDefinition {
    private String namespace;
    private boolean elementFormQualified;
    private boolean attributeFormQualified;
    private Map<String, ElementDefinition> elements = new HashMap<>();
    private Map<String, ComplexTypeDefinition> complexTypes = new HashMap<>();
    private Map<String, SimpleTypeDefinition> simpleTypes = new HashMap<>();
    private Map<String, SchemaDefinition> imports = new HashMap<>();
    private Schema schema;

    public SchemaDefinition(Schema schema) {
        this.schema = schema;
        this.parseSchema();
    }

    public String getName() {
        return ApexUtility.getApexClassFromNamespace(this.getNamespace());
    }

    public void addElement(ElementDefinition ed) {
        this.elements.put(ed.getName(), ed);
    }

    public void addComplexType(ComplexTypeDefinition td) {
        this.complexTypes.put(td.getName(), td);
    }

    public void addSimpleType(SimpleTypeDefinition td) {
        simpleTypes.put(td.getName(), td);
    }

    public void addImport(SchemaDefinition sd) {
        imports.put(sd.getNamespace(), sd);
    }

    public Map<String, SchemaDefinition> getImports() {
        return imports;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isElementFormQualified() {
        return elementFormQualified;
    }

    public void setElementFormQualified(boolean elementFormQualified) {
        this.elementFormQualified = elementFormQualified;
    }

    public boolean isAttributeFormQualified() {
        return attributeFormQualified;
    }

    public void setAttributeFormQualified(boolean attributeFormQualified) {
        this.attributeFormQualified = attributeFormQualified;
    }

    public Map<String, SimpleTypeDefinition> getSimpleTypes() {
        return simpleTypes;
    }

    public Map<String, ComplexTypeDefinition> getComplexTypes() {
        return complexTypes;
    }

    public Map<String, ElementDefinition> getElements() {
        return elements;
    }

    private void parseSchema() {
        this.setNamespace(schema.getTargetNamespace());
        this.setElementFormQualified(schema.getElementFormDefault() != null && schema.getElementFormDefault().equals("qualified"));
        this.setAttributeFormQualified(schema.getAttributeFormDefault() != null && schema.getAttributeFormDefault().equals("qualified"));
        this.processSimpleTypes();
        this.processComplexTypes();
        this.processElements();
        ApexUtility.addSchema(this.getNamespace(), this);
    }

    private void processSimpleTypes() {
        for (SimpleType st : schema.getSimpleTypes()) {
            this.addSimpleType(new SimpleTypeDefinition(st, this));
        }
    }

    private void processComplexTypes() {
        for (ComplexType ct : schema.getComplexTypes()) {
            this.addComplexType(new ComplexTypeDefinition(ct, this));
        }
    }

    private void processElements() {
        for (Element e : schema.getElements()) {
            this.addElement(new ElementDefinition(e, this));
        }
    }
}
