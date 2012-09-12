package com.googlecode.wmbutil.messages.factories;

import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class DefaultMutableElementWrapperFactory extends MutableElementWrapperFactory<MbElementWrapper> {
  protected MbElementWrapper createWrapper(MbElement element) {
    return new MbElementWrapper(element) {};
  }
}
