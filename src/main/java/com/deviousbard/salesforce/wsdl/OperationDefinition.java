package com.deviousbard.salesforce.wsdl;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.SchemaComponent;
import com.predic8.wsdl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OperationDefinition {
    private String name;
    private String inputMessageName;
    private String outputMessageName;
    private String inputMessageNamespace;
    private String outputMessageNamespace;
    private String inputHeaderName;
    private String inputHeaderType;
    private String inputHeaderTypeNamespace;
    private String inputHeaderNamespace;
    private String outputHeaderName;
    private String outputHeaderNamespace;
    private String outputHeaderType;
    private String outputHeaderTypeNamespace;
    private String soapAction;
    private Map<String, String> bindingInputParts = new HashMap<>();

    public OperationDefinition(PortType portType, BindingOperation bindingOperation, Message inputMessage, Message outputMessage) {
        this.name = bindingOperation.getName();
        BindingInput bindingInput = bindingOperation.getInput();
        BindingOutput bindingOutput = bindingOperation.getOutput();
        for (BindingElement be : bindingInput.getBindingElements()) {
            if (be instanceof AbstractSOAPBody) {
                this.inputMessageName = ((AbstractSOAPBody)be).getParts().get(0).getElement().getName();
                this.inputMessageNamespace = ((AbstractSOAPBody)be).getParts().get(0).getElement().getNamespaceUri();
            } if (be instanceof AbstractSOAPHeader)  {
                this.inputHeaderName = ((AbstractSOAPHeader)be).getPart().getElement().getName();
                this.inputHeaderNamespace = ((AbstractSOAPHeader)be).getPart().getElement().getNamespaceUri();
                this.inputHeaderTypeNamespace = ApexUtility.getApexClassFromNamespace(((AbstractSOAPHeader)be).getPart().getElement().getType().getNamespaceURI());
                this.inputHeaderType = ((AbstractSOAPHeader)be).getPart().getElement().getType().getLocalPart();
            }
        }
        for (BindingElement be : bindingOutput.getBindingElements()) {
            if (be instanceof AbstractSOAPBody) {
                this.outputMessageName = ((AbstractSOAPBody)be).getParts().get(0).getElement().getName();
                this.outputMessageNamespace = ((AbstractSOAPBody)be).getParts().get(0).getElement().getNamespaceUri();
            } if (be instanceof AbstractSOAPHeader)  {
                this.outputHeaderName = ((AbstractSOAPHeader)be).getPart().getElement().getName();
                this.outputHeaderNamespace = ((AbstractSOAPHeader)be).getPart().getElement().getNamespaceUri();
                this.outputHeaderTypeNamespace = ApexUtility.getApexClassFromNamespace(((AbstractSOAPHeader)be).getPart().getElement().getType().getNamespaceURI());
                this.outputHeaderType = ((AbstractSOAPHeader)be).getPart().getElement().getType().getLocalPart();
            }
        }
        this.soapAction = bindingOperation.getOperation().getSoapAction();
        Part inputMessagePart = null;
        Element inputElement = null;
        if (inputMessage.getParts().size() > 0) {
            inputMessagePart = inputMessage.getParts().get(0);
            inputElement = inputMessagePart.getElement();
        }
        if (inputElement != null) {
            if (inputElement.getEmbeddedType() != null) {
                for (SchemaComponent sc : ((ModelGroup) ((ComplexType)inputElement.getEmbeddedType()).getModel()).getParticles()) {
                    if (sc.getClass().getSimpleName().equals("Element")) {
                        Element el = (Element)sc;
                        bindingInputParts.put(el.getName(), ApexUtility.getApexClassFromNamespace(el.getType().getNamespaceURI()) + "." + el.getType().getLocalPart() + " " + el.getName());
                    }
                }
            }
        }
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

    public Set<String> getInputParameterElementNames() {
        return bindingInputParts.keySet();
    }

    public String getInputHeaderName() {
        return inputHeaderName;
    }

    public void setInputHeaderName(String inputHeaderName) {
        this.inputHeaderName = inputHeaderName;
    }

    public String getInputHeaderNamespace() {
        return inputHeaderNamespace;
    }

    public void setInputHeaderNamespace(String inputHeaderNamespace) {
        this.inputHeaderNamespace = inputHeaderNamespace;
    }

    public String getOutputHeaderName() {
        return outputHeaderName;
    }

    public void setOutputHeaderName(String outputHeaderName) {
        this.outputHeaderName = outputHeaderName;
    }

    public String getOutputHeaderNamespace() {
        return outputHeaderNamespace;
    }

    public void setOutputHeaderNamespace(String outputHeaderNamespace) {
        this.outputHeaderNamespace = outputHeaderNamespace;
    }

    public Map<String, String> getBindingInputParts() {
        return bindingInputParts;
    }

    public void setBindingInputParts(Map<String, String> bindingInputParts) {
        this.bindingInputParts = bindingInputParts;
    }

    public String getInputParameterElementValue(String inputParameterElementName) {
        return bindingInputParts.get(inputParameterElementName);
    }

    public String getInputHeaderType() {
        return inputHeaderType;
    }

    public void setInputHeaderType(String inputHeaderType) {
        this.inputHeaderType = inputHeaderType;
    }

    public String getOutputHeaderType() {
        return outputHeaderType;
    }

    public void setOutputHeaderType(String outputHeaderType) {
        this.outputHeaderType = outputHeaderType;
    }

    public String getInputHeaderTypeNamespace() {
        return inputHeaderTypeNamespace;
    }

    public void setInputHeaderTypeNamespace(String inputHeaderTypeNamespace) {
        this.inputHeaderTypeNamespace = inputHeaderTypeNamespace;
    }

    public String getOutputHeaderTypeNamespace() {
        return outputHeaderTypeNamespace;
    }

    public void setOutputHeaderTypeNamespace(String outputHeaderTypeNamespace) {
        this.outputHeaderTypeNamespace = outputHeaderTypeNamespace;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Operation[");
        sb.append("name='").append(name).append("'; inputMessageNamespace='").append(inputMessageNamespace)
                .append("'; inputMessageName='").append(inputMessageName)
                .append("'; outputMessageNamespace='").append(outputMessageNamespace)
                .append("'; outputMessageName='").append(outputMessageName)
                .append("'; soapAction='").append(soapAction)
                .append("'; bindingInputParts=").append(bindingInputParts);
        sb.append("]");
        return sb.toString();
    }
}
