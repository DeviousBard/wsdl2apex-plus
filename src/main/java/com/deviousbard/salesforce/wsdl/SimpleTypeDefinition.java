package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.restriction.facet.EnumerationFacet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleTypeDefinition {
    private String name;
    private String namespace;
    private int maxLength = -1;
    private int minLength = -1;
    private String base;
    private Set<String> enumerations = new HashSet<>();

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

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public Set<String> getEnumerations() {
        return enumerations;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setEnumerations(Set<String> enumerations) {
        this.enumerations = enumerations;
    }

    public void addEnumerationList(List<EnumerationFacet> enumerationList) {
        for (EnumerationFacet ef : enumerationList) {
            enumerations.add(ef.getValue());
        }
    }

    public boolean isEnumerated() {
        return this.enumerations.size() > 0;
    }

    public boolean isMinLengthRestricted() {
        return this.minLength > -1;
    }

    public boolean isMaxLengthRestricted() {
        return this.maxLength > -1;
    }
}
