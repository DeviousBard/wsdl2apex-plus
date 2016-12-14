package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.PortType;

import java.util.ArrayList;
import java.util.List;

public class ServiceDefinition {
    private String name;
    private String namespace;
    private String endPoint;
    private List<OperationDefinition> operations = new ArrayList<>();

    public ServiceDefinition(Port port, Binding binding, PortType portType) {
        this.name = port.getName();
        this.namespace = portType.getNamespaceUri();
        this.endPoint = port.getAddress().getLocation();
        for (Operation operation : portType.getOperations()) {
            operations.add(new OperationDefinition(portType, binding.getOperation(operation.getName()), operation.getInput().getMessage(), operation.getOutput().getMessage()));
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

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public List<OperationDefinition> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationDefinition> operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ServiceDefinition[");
        sb.append("name='").append(name).append("'; namespace='").append(namespace)
                .append("'; endpoint='").append(endPoint).append("'; operations=").append(operations);
        sb.append("]");
        return sb.toString();
    }
}
