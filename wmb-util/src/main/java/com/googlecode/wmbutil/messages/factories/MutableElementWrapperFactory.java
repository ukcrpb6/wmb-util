package com.googlecode.wmbutil.messages.factories;

import com.google.common.base.Optional;
import com.googlecode.wmbutil.NiceMbException;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbXPath;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class MutableElementWrapperFactory<T extends MbElementWrapper> extends ImmutableElementWrapperFactory<T> {

    public T getOrCreateElement(MbMessage message) throws MbException {
        return evaluateXPath(message, getSelectOrCreateXPath());
    }

    public Optional<T> tryRemove(MbMessage msg) throws MbException {
        T elm = get(msg);
        if (elm != null) {
            elm.getMbElement().detach();
            return Optional.of(elm);
        }
        return Optional.absent();
    }

    /**
     * Remove the element from the message.
     *
     * @param msg Message to remove element from
     * @return Detached element
     * @throws MbException Element does not exist
     */
    public T remove(MbMessage msg) throws MbException {
        Optional<T> element = tryRemove(msg);
        if (element.isPresent()) {
            return element.get();
        }
        throw new NiceMbException(getPath() + " does not exist");
    }

    protected MbXPath getSelectOrCreateXPath() throws MbException {
        return new MbXPath(getOrCreatePath());
    }

    protected abstract String getOrCreatePath();

}
