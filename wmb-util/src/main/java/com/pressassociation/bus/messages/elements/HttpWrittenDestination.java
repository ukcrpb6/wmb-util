package com.pressassociation.bus.messages.elements;

import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyRoot;
import com.pressassociation.bus.data.KeyedData;

@KeyRoot("WrittenDestination/HTTP")
public interface HttpWrittenDestination extends KeyedData {

    static final String REQUEST_URL = "RequestURL";

    @Key(REQUEST_URL)
    public String getRequestUrl() throws MbException;

    @Key(REQUEST_URL)
    public void setRequestUrl(String requestUrl) throws MbException;

}
