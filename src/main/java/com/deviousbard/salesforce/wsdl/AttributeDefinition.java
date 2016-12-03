package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.Attribute;

public class AttributeDefinition {

    private String name;
    private String namespace;
    private String fixedValue;
    private String defaultValue;
    private String use;

    public AttributeDefinition(Attribute at, SchemaDefinition sd) {
        this.setName(at.getName());
        this.setNamespace(at.getNamespaceUri());
        this.setFixedValue(at.getFixedValue());
        this.setDefaultValue(at.getDefaultValue());
        this.setUse(at.getUse());
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

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AttributeDefinition[name='").append(getName()).append("'; namespace='").append(getNamespace())
                .append("'; use='").append(getUse()).append("'; fixedValue='").append(getFixedValue())
                .append("'; defaultValue='").append(getDefaultValue())
                .append("']");
        return sb.toString();
    }
}
