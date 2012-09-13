package com.googlecode.wmbutil.messages.localenvironment;

import com.google.common.base.Optional;
import com.googlecode.wmbutil.messages.DefaultMbElementWrapper;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.localenvironment.destination.HttpDestination;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class DestinationElementWrapper extends MbElementWrapper {

    public DestinationElementWrapper(MbElement elm) {
        super(elm);
    }

    public Optional<DefaultMbElementWrapper> getFTE() throws MbException { return tryGetField("FTE"); }

    public Optional<HttpDestination> getHTTP() throws MbException { return tryGetField("HTTP", HttpDestination.class); }

    public Optional<DefaultMbElementWrapper> getMQ() throws MbException { return tryGetField("MQ"); }

    public Optional<DefaultMbElementWrapper> getJMS() throws MbException { return tryGetField("JMS"); }

    public Optional<DefaultMbElementWrapper> getFile() throws MbException { return tryGetField("File"); }

    public Optional<DefaultMbElementWrapper> getEmail() throws MbException { return tryGetField("Email"); }

    public Optional<DefaultMbElementWrapper> getSOAP() throws MbException { return tryGetField("SOAP"); }

    public Optional<DefaultMbElementWrapper> getTCPIP() throws MbException { return tryGetField("TCPIP"); }

}
