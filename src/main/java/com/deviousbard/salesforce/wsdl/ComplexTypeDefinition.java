package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.*;

import java.util.ArrayList;
import java.util.List;

public class ComplexTypeDefinition {
    private String name;
    private String namespace;
    private List<ElementDefinition> elements = new ArrayList<>();
    private List<AttributeDefinition> attributes = new ArrayList<>();

    public ComplexTypeDefinition(ComplexType ct, SchemaDefinition sd) {
        this.parseComplexType(ct, sd, null);
    }

    public ComplexTypeDefinition(ComplexType ct, SchemaDefinition sd, String elementName) {
        this.parseComplexType(ct, sd, elementName);
    }

    private void parseComplexType(ComplexType ct, SchemaDefinition sd, String elementName) {
        if (elementName != null) {
            this.setName(elementName + "_element");
        } else {
            this.setName(ct.getName());
        }
        this.setNamespace(ct.getNamespaceUri());
        for (SchemaComponent sc : ((ModelGroup) ct.getModel()).getParticles()) {
            if (sc.getClass().getSimpleName().equals("Element")) {
                addElement(new ElementDefinition((Element) sc, sd));
            }
        }
        for (Attribute at : ct.getAllAttributes()) {
            this.addAttribute(new AttributeDefinition(at, sd));
        }
    }

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

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinition> attributes) {
        this.attributes = attributes;
    }

    public void addElement(ElementDefinition ed) {
        this.elements.add(ed);
    }

    public void addAttribute(AttributeDefinition ad) {
        this.attributes.add(ad);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ComplexTypeDefinition[name='").append(getName()).append("'; namespace='").append(getNamespace())
                .append("; elements=").append(getElements()).append("; attributes=").append(getAttributes())
                .append("]");
        return sb.toString();
    }
}
