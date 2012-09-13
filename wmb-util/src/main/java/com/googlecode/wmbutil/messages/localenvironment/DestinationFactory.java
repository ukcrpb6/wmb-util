package com.googlecode.wmbutil.messages.localenvironment;

import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.factories.MutableElementWrapperFactory;
import com.googlecode.wmbutil.messages.localenvironment.destination.HttpDestination;
import com.ibm.broker.plugin.MbElement;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class DestinationFactory<T extends MbElementWrapper> extends MutableElementWrapperFactory<T> {

    public static class DefaultDestinationFactory extends DestinationFactory<MbElementWrapper> {
        private DefaultDestinationFactory(String relativePath) {
            super(relativePath);
        }

        @Override protected MbElementWrapper createWrapper(MbElement element) {
            return new MbElementWrapper(element) {};
        }
    }

    public static final DestinationFactory FTE = new DefaultDestinationFactory("FTE");
    public static final DestinationFactory<HttpDestination> HTTP = new DestinationFactory<HttpDestination>("HTTP") {
        @Override protected HttpDestination createWrapper(MbElement element) {
            return new HttpDestination(element);
        }
    };
    public static final DestinationFactory MQ = new DefaultDestinationFactory("MQ");
    public static final DestinationFactory JMS = new DefaultDestinationFactory("JMS");
    public static final DestinationFactory FILE = new DefaultDestinationFactory("FILE");
    public static final DestinationFactory EMAIL = new DefaultDestinationFactory("Email");
    public static final DestinationFactory SOAP = new DefaultDestinationFactory("SOAP");
    public static final DestinationFactory TCPIP = new DefaultDestinationFactory("TCPIP");

    private final String relativePath;

    private DestinationFactory(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override protected String getPath() {
        return "Destination/" + relativePath;
    }

    @Override protected String getOrCreatePath() {
        return "?Destination/?" + relativePath;
    }

}
