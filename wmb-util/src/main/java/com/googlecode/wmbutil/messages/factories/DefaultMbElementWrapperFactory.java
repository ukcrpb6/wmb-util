package com.googlecode.wmbutil.messages.factories;

import com.google.common.collect.ImmutableList;
import com.googlecode.wmbutil.messages.DefaultMbElementWrapper;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class DefaultMbElementWrapperFactory implements MbElementWrapperFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MbElementWrapper> T getAdapter(MbElement adaptable, Class<T> adapterType) {
        checkArgument(DefaultMbElementWrapper.class.equals(adapterType), "Adapter only supports " + getAdapterList());
        return (T) new DefaultMbElementWrapper(checkNotNull(adaptable));
    }

    @Override
    public List<Class<? extends MbElementWrapper>> getAdapterList() {
        return ImmutableList.<Class<? extends MbElementWrapper>>of(DefaultMbElementWrapper.class);
    }

}
