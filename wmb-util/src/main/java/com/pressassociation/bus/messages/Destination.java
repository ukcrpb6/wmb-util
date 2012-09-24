package com.pressassociation.bus.messages;

import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyRoot;
import com.pressassociation.bus.data.KeyedData;

@KeyRoot("/Destination")
public interface Destination extends KeyedData {

    @Key("HTTP")
    public HttpDestination getHttp() throws MbException;

}
