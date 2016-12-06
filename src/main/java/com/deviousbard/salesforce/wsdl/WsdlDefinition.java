package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.Definitions;

public class WsdlDefinition {
    private String namespace;

    public WsdlDefinition(Definitions defs) {
        this.parseWsdl(defs);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private void parseWsdl(Definitions defs) {
        this.setNamespace(defs.getTargetNamespace());
        ApexUtility.addWsdl(this.getNamespace(), this);
    }
}
