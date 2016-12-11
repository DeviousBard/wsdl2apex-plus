package com.deviousbard.salesforce.wsdl;

import com.predic8.wsdl.AbstractSOAPBinding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.PortType;

import java.util.ArrayList;
import java.util.List;

public class OperationDefinition {
    private String name;
    private String inputMessageName;
    private String outputMessageName;
    private String inputMessageNamespace;
    private String outputMessageNamespace;
    private String soapAction;
    private List<String> inputParameterElementNames = new ArrayList<>();

    public OperationDefinition(BindingOperation bindingOperation, Message inputMessage, Message outputMessage) {
        this.name = bindingOperation.getName();
        this.inputMessageName = inputMessage.getQName().getLocalPart();
        this.inputMessageNamespace = inputMessage.getQName().getNamespaceURI();
        this.outputMessageName = outputMessage.getQName().getLocalPart();
        this.outputMessageNamespace = outputMessage.getQname().getNamespaceURI();
        this.soapAction = bindingOperation.getOperation().getSoapAction();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputMessageName() {
        return inputMessageName;
    }

    public void setInputMessageName(String inputMessageName) {
        this.inputMessageName = inputMessageName;
    }

    public String getOutputMessageName() {
        return outputMessageName;
    }

    public void setOutputMessageName(String outputMessageName) {
        this.outputMessageName = outputMessageName;
    }

    public String getInputMessageNamespace() {
        return inputMessageNamespace;
    }

    public void setInputMessageNamespace(String inputMessageNamespace) {
        this.inputMessageNamespace = inputMessageNamespace;
    }

    public String getOutputMessageNamespace() {
        return outputMessageNamespace;
    }

    public void setOutputMessageNamespace(String outputMessageNamespace) {
        this.outputMessageNamespace = outputMessageNamespace;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public List<String> getInputParameterElementNames() {
        return inputParameterElementNames;
    }

    public void setInputParameterElementNames(List<String> inputParameterElementNames) {
        this.inputParameterElementNames = inputParameterElementNames;
    }
}
