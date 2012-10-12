package com.pressassociation.bus.data;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import java.util.List;

public interface KeyedData {
    public boolean exists() throws MbException;

    public MbElement get() throws MbException;

    @Beta
    public Optional<MbElement> find() throws MbException;

    @Beta
    public List<MbElement> findAll() throws MbException;
}
