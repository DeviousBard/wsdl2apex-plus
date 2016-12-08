package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.Service;

import java.util.ArrayList;
import java.util.List;

public class WsdlDefinition {
    private String namespace;
    private List<PortType> portTypes = new ArrayList<>();
    private List<Binding> bindings = new ArrayList<>();
    private List<Service> services = new ArrayList<>();

    public WsdlDefinition(Definitions defs) {
        this.portTypes = defs.getLocalPortTypes();
        this.bindings = defs.getLocalBindings();
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
