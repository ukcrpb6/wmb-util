package com.pressassociation.bus.messages;

import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyedData;

public interface RequestLine extends KeyedData {

    @Key("Method")
    String getMethod() throws MbException;

    @Key("Method")
    void setMethod(String method) throws MbException;

    @Key("RequestURI")
    String getRequestUri() throws MbException;

    @Key("RequestURI")
    void setRequestUri(String uri) throws MbException;

    @Key("HttpVersion")
    String getHttpVersion() throws MbException;

    @Key("HttpVersion")
    void setHttpVersion(String httpVersion) throws MbException;

}
