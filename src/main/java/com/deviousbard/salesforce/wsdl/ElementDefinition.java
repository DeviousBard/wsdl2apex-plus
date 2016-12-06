package com.deviousbard.salesforce.wsdl;


import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import groovy.xml.QName;

public class ElementDefinition {
    private static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    private String name;
    private String elementNamespace;
    private String type;
    private String typeNamespace;
    private boolean required;
    private int minOccurs = 1;
    private int maxOccurs = 1;
    private boolean nilable;
    private SchemaDefinition schemaDefinition;

    public ElementDefinition(Element e, SchemaDefinition sd) {
        parseElement(e, sd);
    }

    private void parseElement(Element el, SchemaDefinition sd) {
        this.schemaDefinition = sd;
        this.setName(el.getName());
        this.setElementNamespace(el.getNamespaceUri());
        QName qualifiedType = el.getType();
        if (qualifiedType != null) {
            this.setType(qualifiedType.getLocalPart());
            this.setTypeNamespace(qualifiedType.getNamespaceURI());
        } else if (el.getEmbeddedType() != null && el.getEmbeddedType() instanceof ComplexType) {
            ComplexTypeDefinition ctd = new ComplexTypeDefinition((ComplexType)el.getEmbeddedType(), sd, this.getName());
            this.setName(ctd.getName());
            sd.addComplexType(ctd);
        }
        this.setMinOccurs(el.getMinOccurs() == null || el.getMinOccurs().equals("") ? 1 : Integer.parseInt(el.getMinOccurs()));
        this.setMaxOccurs(el.getMaxOccurs() == null || el.getMaxOccurs().equals("") ? 1 : el.getMaxOccurs().equals("unbounded") ? -1 : Integer.parseInt(el.getMaxOccurs()));
        this.setRequired(this.getMinOccurs() >= 1);
        this.setNilable(el.isNillable());
    }

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

    public boolean isBaseType() {
        return (this.typeNamespace != null && this.typeNamespace.equals(XML_SCHEMA_NAMESPACE));
    }

    public boolean isSimpleType() {
        SchemaDefinition sd = ApexUtility.getSchemaDefinition(this.getTypeNamespace());
        return sd != null && sd.getSimpleTypes().keySet().contains(this.getType());
    }

    public boolean isComplexType() {
        SchemaDefinition sd = ApexUtility.getSchemaDefinition(this.getTypeNamespace());
        return sd != null && sd.getComplexTypes().keySet().contains(this.getType());
    }

    public String getSimpleTypeApexType() {
        String simpleTypeApexType = "";
        SchemaDefinition sd = ApexUtility.getSchemaDefinition(this.getTypeNamespace());
        if (sd != null) {
            SimpleTypeDefinition std = sd.getSimpleTypes().get(this.getType());
            if (std != null) {
                simpleTypeApexType = ApexUtility.getApexTypeFromBaseType(std.getBase());
            }
        }
        return simpleTypeApexType;
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

    public String getApexType() {
        if (this.isBaseType()) {
            return ApexUtility.getApexTypeFromBaseType("{" + typeNamespace + "}" + type);
        } else {
            return ApexUtility.getApexClassFromNamespace(this.getTypeNamespace()) + "." + this.getType();
       }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ElementDefinition[name='").append(getName()).append("'; elementNamespace='").append(getElementNamespace())
                .append("'; typeNamespace='").append(getTypeNamespace()).append("'; type='").append(getType())
                .append("'; minOccurs=").append(getMinOccurs()).append("; maxOccurs=").append(getMaxOccurs())
                .append("; isRequired=").append(isRequired()).append("; isMultiOccurring=").append(isMultiOccurring())
                .append("; isSimpleType=").append(isSimpleType()).append("; isNilable=").append(isNilable())
                .append("; apexType='").append(getApexType()).append("'; isBaseType=").append(isBaseType())
                .append("; isSimpleType=").append(isSimpleType()).append("; isComplextype=").append(isComplexType())
                .append("]");
        return sb.toString();
    }
}
