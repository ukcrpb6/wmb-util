package com.pressassociation.bus;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;

public class MbMessageAssemblyBuilder {
    private MbMessageAssembly assembly;
    private MbMessage message;
    private MbMessage localEnvironment;
    private MbMessage exceptionList;

    public MbMessageAssemblyBuilder(MbMessageAssembly assembly) {
        this.assembly = Preconditions.checkNotNull(assembly);
    }

    public MbMessageAssembly build() throws MbException {
        return new MbMessageAssembly(assembly, getLocalEnvironment(), getExceptionList(), getMessage());
    }

    public MbMessage copyMessage() throws MbException {
        return this.message = new MbMessage(this.getMessage());
    }

    public MbMessage copyLocalEnvironment() throws MbException {
        return this.localEnvironment = new MbMessage(this.getLocalEnvironment());
    }

    public MbMessage copyExceptionList() throws MbException {
        return this.exceptionList = new MbMessage(this.getExceptionList());
    }

    public MbMessage newMessage() throws MbException {
        return this.message = new MbMessage();
    }

    public MbMessage newLocalEnvironment() throws MbException {
        return this.localEnvironment = new MbMessage();
    }

    public MbMessage newExceptionList() throws MbException {
        return this.exceptionList = new MbMessage();
    }

    public MbMessage getMessage() throws MbException {
        return message == null ? assembly.getMessage() : message;
    }

    public void setMessage(MbMessage message) {
        this.message = message;
    }

    public MbMessage getLocalEnvironment() throws MbException {
        return localEnvironment == null ? assembly.getLocalEnvironment() : localEnvironment;
    }

    public void setLocalEnvironment(MbMessage localEnvironment) {
        this.localEnvironment = localEnvironment;
    }

    public MbMessage getGlobalEnvironment() throws MbException {
        return assembly.getGlobalEnvironment();
    }

    public MbMessage getExceptionList() throws MbException {
        return exceptionList == null ? assembly.getExceptionList() : exceptionList;
    }

    public void setExceptionList(MbMessage exceptionList) {
        this.exceptionList = exceptionList;
    }

    public MbMessageAssemblyBuilder withNewMessage() throws MbException {
        newMessage();
        return this;
    }

    public MbMessageAssemblyBuilder withNewLocalEnvironment() throws MbException {
        newLocalEnvironment();
        return this;
    }

    public MbMessageAssemblyBuilder withNewExceptionList() throws MbException {
        newExceptionList();
        return this;
    }

    public MbMessageAssemblyBuilder withCopiedMessage() throws MbException {
        copyMessage();
        return this;
    }

    public MbMessageAssemblyBuilder withCopiedLocalEnvironment() throws MbException {
        copyLocalEnvironment();
        return this;
    }

    public MbMessageAssemblyBuilder withCopiedExceptionList() throws MbException {
        copyExceptionList();
        return this;
    }

    @Override public String toString() {
        return Objects.toStringHelper(getClass())
                .add("assembly", assembly)
                .add("message", message)
                .add("localEnvironment", localEnvironment)
                .add("exceptionList", exceptionList)
                .omitNullValues().toString();
    }
}