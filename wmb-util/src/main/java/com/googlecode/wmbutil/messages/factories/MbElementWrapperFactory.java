package com.googlecode.wmbutil.messages.factories;

import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;

import java.util.List;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public interface MbElementWrapperFactory {
    <T extends MbElementWrapper> T getAdapter(MbElement adaptable, Class<T> adapterType);

    List<Class<? extends MbElementWrapper>> getAdapterList();
}
