package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.SimpleType;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.facet.EnumerationFacet;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.PatternFacet;
import groovy.xml.QName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleTypeDefinition {
    private String name;
    private String namespace;
    private int maxLength = -1;
    private int minLength = -1;
    private int length = -1;
    private String base;
    private String regexPattern;
    private Set<String> enumerations = new HashSet<>();

    public SimpleTypeDefinition(SimpleType st, SchemaDefinition sd) {
        parseSimpleType(st, sd);
    }

    private void parseSimpleType(SimpleType st, SchemaDefinition sd) {
        this.setName(st.getName());
        this.setNamespace(st.getNamespaceUri());
        BaseRestriction br = st.getRestriction();
        if (br != null) {
            QName base = br.getBase();
            if (base != null) {
                this.setBase(base.toString());
            }
            if (br.hasEnumerationFacet()) {
                this.addEnumerationList(br.getEnumerationFacets());
            }
            if (br.getMinLengthFacet() != null) {
                String value = br.getMinLengthFacet().getValue();
                if (value != null) {
                    this.setMinLength(value.equals("") ? -1 : Integer.parseInt(value));
                }
            }
            if (br.getMaxLengthFacet() != null) {
                String value = br.getMaxLengthFacet().getValue();
                if (value != null) {
                    this.setMaxLength(value.equals("") ? -1 : Integer.parseInt(value));
                }
            }
            if (br.getLengthFacet() != null) {
                String value = br.getLengthFacet().getValue();
                if (value != null) {
                    this.setLength(value.equals("") ? -1 : Integer.parseInt(value));
                }
            }
            PatternFacet patternFacet = null;
            for (Facet facet : br.getFacets()) {
                if (facet instanceof PatternFacet) {
                    patternFacet = (PatternFacet)facet;
                    break;
                }
            }
            if (patternFacet != null) {
                this.setRegexPattern(patternFacet.getValue());
            }
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

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getRegexPattern() {
        return (regexPattern == null ? null : regexPattern.replace("\\", "\\\\"));
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
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

    public boolean isLengthRestricted() {
        return this.length > -1;
    }

    public boolean isMaxLengthRestricted() {
        return this.maxLength > -1;
    }

    public boolean isRegexPatternRestricted() {return this.regexPattern != null;}

    public String getApexType() {
        return ApexUtility.getApexTypeFromBaseType(this.base);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleTypeDefinition[base='").append(getBase()).append("'; name='").append(getName()).append("'; namespace='").append(getNamespace())
                .append("'; minLength=").append(getMinLength()).append("; maxLength=").append(getMaxLength()).append("; length=").append(getLength())
                .append("; regexPattern='").append(getRegexPattern()).append("'; apexType='").append(getApexType())
                .append("'; enumerations=").append(getEnumerations()).append("]");
        return sb.toString();
    }
}
