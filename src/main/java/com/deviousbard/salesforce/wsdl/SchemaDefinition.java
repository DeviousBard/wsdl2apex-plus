package com.deviousbard.salesforce.wsdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaDefinition {
    private String name;
    private String namespace;
    private List<ComplexTypeDefinition> complexTypes = new ArrayList<>();
    private Map<String, SimpleTypeDefinition> simpleTypes = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ComplexTypeDefinition> getComplexTypes() {
        return complexTypes;
    }

    public void setComplexTypes(List<ComplexTypeDefinition> complexTypes) {
        this.complexTypes = complexTypes;
    }

    public void addComplexType(ComplexTypeDefinition td) {
        this.complexTypes.add(td);
    }

    public void addSimpleType(SimpleTypeDefinition td) {
        this.simpleTypes.put(td.getName(), td);
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

    public void setSimpleTypes(Map<String, SimpleTypeDefinition> simpleTypes) {
        this.simpleTypes = simpleTypes;
    }

    public String getApexType(ElementDefinition ed, boolean qualified) {
        StringBuilder apexType = new StringBuilder();
        if (ed.isPrimitive()) {
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
}
