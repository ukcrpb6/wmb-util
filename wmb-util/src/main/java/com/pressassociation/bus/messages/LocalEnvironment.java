package com.pressassociation.bus.messages;

import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyedData;

public interface LocalEnvironment extends KeyedData {

    @Key("Destination")
    Destination getDestination() throws MbException;

}
