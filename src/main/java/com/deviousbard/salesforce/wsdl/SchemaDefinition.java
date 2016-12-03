package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SimpleType;

import java.util.HashMap;
import java.util.Map;

public class SchemaDefinition {
    private String name;
    private String namespace;
    private Map<String, ElementDefinition> elements = new HashMap<>();
    private Map<String, ComplexTypeDefinition> complexTypes = new HashMap<>();
    private Map<String, SimpleTypeDefinition> simpleTypes = new HashMap<>();
    private Map<String, SchemaDefinition> imports = new HashMap<>();
    private Schema schema;
    private Map<String, String> classNameMap = new HashMap<>();

    public SchemaDefinition(Schema schema, Map<String, String> classNameMap) {
        this.schema = schema;
        this.classNameMap = classNameMap;
        this.parseSchema();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Map<String, SimpleTypeDefinition> getSimpleTypes() {
        return simpleTypes;
    }

    public Map<String, ComplexTypeDefinition> getComplexTypes() {
        return complexTypes;
    }

    public Map<String, ElementDefinition> getElements() {
        return elements;
    }

    public String getApexType(ElementDefinition ed, boolean qualified) {
        StringBuilder apexType = new StringBuilder();
        if (ed.isSimpleType()) {
            apexType.append(getPrimitiveApexType(ed.getType()));
        } else {
            apexType.append(qualified ? getName() : "").append(qualified ? "." : "").append(ed.getType());
        }
        return apexType.toString();
    }

    public String getPrimitiveApexType(String type) {
        String lType = type.toLowerCase();
        StringBuilder primitiveApexType = new StringBuilder();
        if (lType.contains("string") || lType.contains("token")) {
            primitiveApexType.append("String");
        } else if (lType.contains("int")) {
            primitiveApexType.append("Integer");
        } else if (lType.contains("datetime")) {
            primitiveApexType.append("DateTime");
        } else if (lType.contains("date")) {
            primitiveApexType.append("Date");
        } else if (lType.contains("decimal")) {
            primitiveApexType.append("Decimal");
        }
        return primitiveApexType.toString();
    }

    public boolean isSimpleType(String typeName) {
        return simpleTypes.containsKey(typeName);
    }

    public SimpleTypeDefinition getSimpleType(String typeName) {
        return simpleTypes.get(typeName);
    }

    private void parseSchema() {
        this.setName(schema.getName());
        this.setNamespace(schema.getTargetNamespace());
        this.processSimpleTypes();
        this.processComplexTypes();
        this.processElements();
        schema.getImportedSchemas();
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
