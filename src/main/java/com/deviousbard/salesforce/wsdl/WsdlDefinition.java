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
        this.bindings = defs.getBindings();
        this.portTypes = defs.getPortTypes();
        this.services = defs.getServices();
        this.parseWsdl(defs);
    }

    public List<PortType> getPortTypes() {
        return portTypes;
    }

    public void setPortTypes(List<PortType> portTypes) {
        this.portTypes = portTypes;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WsdlDefinition[");
        sb.append("namespace='").append(namespace).append("'; portTypes=").append(portTypes)
                .append("; bindings=").append(bindings).append("; services=").append(services);
        return sb.toString();
    }
}
