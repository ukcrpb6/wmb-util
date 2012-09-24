package com.pressassociation.bus;

import com.google.common.base.Preconditions;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

public class MbElementBuilder {

    private MbElement element;

    public MbElementBuilder(MbElement element) {
        this.element = element;
    }

    public static MbElementBuilder extend(MbElement element) {
        return new MbElementBuilder(element);
    }

    public MbElementBuilder getFirstChild() throws MbException {
        this.element = Preconditions.checkNotNull(element.getFirstChild());
        return this;
    }

    public MbElementBuilder createElementBefore(int type) throws MbException {
        element = element.createElementBefore(type);
        return this;
    }

    public MbElementBuilder createElementBefore(String name) throws MbException {
        element = element.createElementBefore(name);
        return this;
    }

    public MbElementBuilder createElementBefore(int type, String name, Object value) throws MbException {
        element = element.createElementBefore(type, name, value);
        return this;
    }

    public MbElementBuilder createElementAfter(int type) throws MbException {
        element = element.createElementAfter(type);
        return this;
    }

    public MbElementBuilder createElementAfter(String name) throws MbException {
        element = element.createElementAfter(name);
        return this;
    }

    public MbElementBuilder createElementAfter(int type, String name, Object value) throws MbException {
        element = element.createElementAfter(type, name, value);
        return this;
    }

    public MbElementBuilder createElementAsFirstChild(int type) throws MbException {
        element = element.createElementAsFirstChild(type);
        return this;
    }

    public MbElementBuilder createElementAsFirstChild(String name) throws MbException {
        element = element.createElementAsFirstChild(name);
        return this;
    }

    public MbElementBuilder createElementAsFirstChild(int type, String name, Object value) throws MbException {
        element = element.createElementAsFirstChild(type, name, value);
        return this;
    }

    public MbElementBuilder createElementAsLastChild(int type) throws MbException {
        element = element.createElementAsLastChild(type);
        return this;
    }

    public MbElementBuilder createElementAsLastChild(String name) throws MbException {
        element = element.createElementAsLastChild(name);
        return this;
    }

    public MbElementBuilder createElementAsLastChild(int type, String name, Object value) throws MbException {
        element = element.createElementAsLastChild(type, name, value);
        return this;
    }

    public MbElementBuilder up() throws MbException {
        element = Preconditions.checkNotNull(element.getParent());
        return this;
    }

}
