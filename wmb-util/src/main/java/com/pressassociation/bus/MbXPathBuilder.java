package com.pressassociation.bus;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbXPath;

public class MbXPathBuilder {
    private MbXPath xpath;

    private MbXPathBuilder(String xpath) throws MbException {
        this.xpath = new MbXPath(xpath);
    }

    private MbXPathBuilder(String xpath, MbElement element) throws MbException {
        this.xpath = new MbXPath(xpath, element);
    }

    public static MbXPathBuilder builder(String xpath) throws MbException {
        return new MbXPathBuilder(xpath);
    }

    public static MbXPathBuilder builder(String xpath, MbElement element) throws MbException {
        return new MbXPathBuilder(xpath, element);
    }

    public MbXPathBuilder setDefaultNamespace(String defaultNamespace) {
        xpath.setDefaultNamespace(defaultNamespace);
        return this;
    }

    public MbXPathBuilder addNamespacePrefix(String prefix, String namespace) {
        xpath.addNamespacePrefix(prefix, namespace);
        return this;
    }

    public MbXPathBuilder assignVariable(String variableName, boolean value) {
        xpath.assignVariable(variableName, value);
        return this;
    }

    public MbXPathBuilder assignVariable(String variableName, double value) {
        xpath.assignVariable(variableName, value);
        return this;
    }

    public MbXPathBuilder assignVariable(String variableName, Object value) {
        xpath.assignVariable(variableName, value);
        return this;
    }

    public MbXPathBuilder removeAllVariables() {
        xpath.removeAllVariables();
        return this;
    }

    public MbXPathBuilder removeVariable(String variableName) {
        xpath.removeVariable(variableName);
        return this;
    }

    public MbXPathBuilder removeNamespacePrefix(String prefix) {
        xpath.removeNamespacePrefix(prefix);
        return this;
    }

    public MbXPath build() {
        return xpath;
    }
}
