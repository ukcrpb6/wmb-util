package com.googlecode.wmbutil.messages.localenvironment.writtendestination;

import com.googlecode.wmbutil.CCSID;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import java.util.Map;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class HttpWrittenDestination extends MbElementWrapper {
  public HttpWrittenDestination(MbElement elm) {
    super(elm);
  }
}
