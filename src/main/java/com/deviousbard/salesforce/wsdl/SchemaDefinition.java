package com.deviousbard.salesforce.wsdl;

import java.util.ArrayList;
import java.util.List;

public class SchemaDefinition {
    private String name;
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
        if (ed.getType().equals("xsd:string")) {
            primitiveApexType.append("String");
        }
        return primitiveApexType.toString();
    }
}
