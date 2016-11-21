package com.deviousbard.salesforce.wsdl;

import java.util.ArrayList;
import java.util.List;

public class SchemaDefinition {
    private String name;
    private String namespace;
    private List<TypeDefinition> types = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeDefinition> getTypes() {
        return types;
    }

    public void setTypes(List<TypeDefinition> types) {
        this.types = types;
    }

    public void addType(TypeDefinition td) {
        this.types.add(td);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getApexType(TypeDefinition td, ElementDefinition ed, boolean qualified) {
        StringBuilder apexType = new StringBuilder();
        if (ed.isPrimitive()) {
            apexType.append(getPrimitiveApexType(ed));
        } else {
            apexType.append(qualified ? getName() : "").append(qualified ? "." : "").append(ed.getType());
        }
        return apexType.toString();
    }

    private String getPrimitiveApexType(ElementDefinition ed) {
        StringBuilder primitiveApexType = new StringBuilder();
        if (ed.getType().toLowerCase().contains("string")) {
            primitiveApexType.append("String");
        } else if (ed.getType().toLowerCase().contains("int")) {
            primitiveApexType.append("Integer");
        } else if (ed.getType().toLowerCase().contains("date")) {
            primitiveApexType.append("Date");
        } else if (ed.getType().toLowerCase().contains("datetime")) {
            primitiveApexType.append("DateTime");
        }
        return primitiveApexType.toString();
    }
}
