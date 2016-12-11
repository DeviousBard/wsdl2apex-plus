package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.PortType;

import java.util.ArrayList;
import java.util.List;

public class ServiceDefinition {
    private String name;
    private String namespace;
    private String endPoint;
    private List<OperationDefinition> operations = new ArrayList<>();

    public ServiceDefinition(Binding binding, PortType portType, List<Message> messages) {
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
}
