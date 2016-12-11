package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.*;

import java.util.ArrayList;
import java.util.List;

public class WsdlDefinition {
    private String namespace;
    private List<ServiceDefinition> services = new ArrayList<>();

    public WsdlDefinition(Definitions defs) {
        this.parseWsdl(defs);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getApexClassName() {
        return ApexUtility.getApexClassFromNamespace(this.namespace);
    }

    private void parseWsdl(Definitions defs) {
        this.setNamespace(defs.getTargetNamespace());
        ApexUtility.addWsdl(this.getNamespace(), this);
        for (Service service : defs.getServices()) {
            for (Binding binding : defs.getBindings()) {
                if (binding.getType() != null && binding.getType().getLocalPart().equals(service.getName())) {
                    for (PortType portType : defs.getPortTypes()) {
                        if (binding.getPortType().getName().equals(portType.getName())) {
                            services.add(new ServiceDefinition(binding, portType, defs.getMessages()));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WsdlDefinition[");
        sb.append("namespace='").append(namespace).append("'; services=").append(services)
        .append("]");
        return sb.toString();
    }
}
