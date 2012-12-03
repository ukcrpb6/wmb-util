package com.pressassociation.bus.utils;

import com.googlecode.wmbutil.messages.header.MbHttpHeader;
import com.ibm.broker.plugin.MbException;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@SuppressWarnings("unused")
public final class MbHttpHeaderHelper {
    private MbHttpHeaderHelper() {}

    private static void configureProxyRequestHeaders(
            MbHttpHeader inputHeader, MbHttpHeader requestHeader) throws MbException {
        requestHeader.setCustomHeader("X-Forwarded-For", inputHeader.getCustomHeader("X-Remote-Host"));
        requestHeader.setCustomHeader("X-Real-IP", inputHeader.getCustomHeader("X-Remote-Addr"));
    }

}