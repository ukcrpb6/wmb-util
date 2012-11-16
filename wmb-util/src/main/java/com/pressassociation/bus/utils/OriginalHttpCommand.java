package com.pressassociation.bus.utils;

import com.googlecode.wmbutil.messages.header.MbHttpHeader;
import com.ibm.broker.plugin.MbException;

public class OriginalHttpCommand {
    private String method;
    private String uri;
    private float httpVersion;

    public OriginalHttpCommand(String method, String uri, float version) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = version;
    }

    public static OriginalHttpCommand parse(MbHttpHeader header) throws MbException {
        return parse(header.getCustomHeader("X-Original-HTTP-Command"));
    }

    public static OriginalHttpCommand parse(String xOriginalHttpCommandHeader) {
        String[] parts = xOriginalHttpCommandHeader.split("\\s+");
        if (parts.length == 3) {
            return new OriginalHttpCommand(parts[0], parts[1], Float.parseFloat(parts[2].substring(parts[2]
                    .lastIndexOf('/') + 1)));
        }
        throw new IllegalArgumentException("Not a valid HTTP command line.");
    }

    public float getHttpVersion() {
        return httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

}
