package com.deviousbard.salesforce.wsdl;

public class ElementDefinition {
    private static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    private String name;
    private String elementNamespace;
    private String type;
    private String typeNamespace;
    private boolean required;
    private int minOccurs;
    private int maxOccurs;
    private boolean nilable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElementNamespace() {
        return elementNamespace;
    }

    public void setElementNamespace(String elementNamespace) {
        this.elementNamespace = elementNamespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeNamespace() {
        return typeNamespace;
    }

    public void setTypeNamespace(String typeNamespace) {
        this.typeNamespace = typeNamespace;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isPrimitive() {
        return (this.typeNamespace != null && this.typeNamespace.equals(XML_SCHEMA_NAMESPACE));
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public boolean isNilable() {
        return nilable;
    }

    public void setNilable(boolean nilable) {
        this.nilable = nilable;
    }

    public boolean isMultiOccurring() {
        return (this.maxOccurs == -1 || maxOccurs > 1);
    }
}
