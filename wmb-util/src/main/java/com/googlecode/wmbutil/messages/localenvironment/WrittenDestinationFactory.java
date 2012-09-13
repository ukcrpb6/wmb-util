package com.googlecode.wmbutil.messages.localenvironment;

import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.factories.ImmutableElementWrapperFactory;
import com.googlecode.wmbutil.messages.localenvironment.writtendestination.HttpWrittenDestination;
import com.ibm.broker.plugin.MbElement;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class WrittenDestinationFactory<T extends MbElementWrapper> extends ImmutableElementWrapperFactory<T> {

    public static class DefaultWrittenDestinationFactory extends WrittenDestinationFactory<MbElementWrapper> {
        private DefaultWrittenDestinationFactory(String relativePath) {
            super(relativePath);
        }

        @Override protected MbElementWrapper createWrapper(MbElement element) {
            return new MbElementWrapper(element) {};
        }
    }

    public static final WrittenDestinationFactory FTE = new DefaultWrittenDestinationFactory("FTE");
    public static final WrittenDestinationFactory<HttpWrittenDestination> HTTP = new WrittenDestinationFactory<HttpWrittenDestination>("HTTP") {
        @Override protected HttpWrittenDestination createWrapper(MbElement element) {
            return new HttpWrittenDestination(element);
        }
    };
    public static final WrittenDestinationFactory MQ = new DefaultWrittenDestinationFactory("MQ");
    public static final WrittenDestinationFactory JMS = new DefaultWrittenDestinationFactory("JMS");
    public static final WrittenDestinationFactory FILE = new DefaultWrittenDestinationFactory("FILE");
    public static final WrittenDestinationFactory EMAIL = new DefaultWrittenDestinationFactory("Email");
    public static final WrittenDestinationFactory SOAP = new DefaultWrittenDestinationFactory("SOAP");
    public static final WrittenDestinationFactory TCPIP = new DefaultWrittenDestinationFactory("TCPIP");

    private final String relativePath;

    private WrittenDestinationFactory(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override protected String getPath() {
        return "/WrittenDestination/" + relativePath;
    }

}
