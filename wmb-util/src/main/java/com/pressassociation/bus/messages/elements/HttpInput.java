package com.pressassociation.bus.messages.elements;

import com.google.common.collect.Multimap;
import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyRoot;
import com.pressassociation.bus.data.KeyedData;

import java.util.Map;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@KeyRoot("HTTP/Input")
public interface HttpInput extends KeyedData {

    static final String QUERY_STRING = "QueryString";
    static final String REQUEST_LINE = "RequestLine";

    @Key(QUERY_STRING)
    public Map<String, String> getQueryString() throws MbException;

    @Key(QUERY_STRING)
    public void setQueryString(Multimap<String, String> queryString) throws MbException;

    @Key(REQUEST_LINE)
    public RequestLine getRequestLine() throws MbException;

}
