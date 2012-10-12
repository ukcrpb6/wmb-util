package com.googlecode.wmbutil.messages.localenvironment;

import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.factories.ImmutableElementWrapperFactory;
import com.googlecode.wmbutil.messages.localenvironment.input.HttpInput;
import com.ibm.broker.plugin.MbElement;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@Deprecated
public abstract class InputFactory<T extends MbElementWrapper> extends ImmutableElementWrapperFactory<T> {

    public static class DefaultInputFactory extends InputFactory<MbElementWrapper> {
        public DefaultInputFactory(String relativePath) {
            super(relativePath);
        }

        @Override protected MbElementWrapper createWrapper(MbElement element) {
            return new MbElementWrapper(element) {};
        }
    }

    public static final InputFactory FTE = new DefaultInputFactory("FTE");
    public static final InputFactory<HttpInput> HTTP = new InputFactory<HttpInput>("HTTP") {
        @Override protected HttpInput createWrapper(MbElement element) {
            return new HttpInput(element);
        }
    };
    public static final InputFactory MQ = new DefaultInputFactory("MQ");
    public static final InputFactory JMS = new DefaultInputFactory("JMS");
    public static final InputFactory FILE = new DefaultInputFactory("FILE");
    public static final InputFactory EMAIL = new DefaultInputFactory("Email");
    public static final InputFactory SOAP = new DefaultInputFactory("SOAP");
    public static final InputFactory TCPIP = new DefaultInputFactory("TCPIP");

    private final String relativePath;

    private InputFactory(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override protected String getPath() {
        return relativePath + "/Input";
    }

}
