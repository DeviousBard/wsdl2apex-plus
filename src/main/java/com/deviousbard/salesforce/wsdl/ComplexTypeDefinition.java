package com.deviousbard.salesforce.wsdl;

import java.util.ArrayList;
import java.util.List;

public class ComplexTypeDefinition {
    private String name;
    private String namespace;
    private List<ElementDefinition> elements = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<ElementDefinition> getElements() {
        return elements;
    }

    public void setElements(List<ElementDefinition> elements) {
        this.elements = elements;
    }

    public void addElement(ElementDefinition ed) {
        this.elements.add(ed);
    }
}
