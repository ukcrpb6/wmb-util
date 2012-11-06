package com.pressassociation.bus.messages.elements;

import com.google.common.collect.Multimap;
import com.ibm.broker.plugin.MbException;
import com.pressassociation.bus.data.Key;
import com.pressassociation.bus.data.KeyRoot;
import com.pressassociation.bus.data.KeyedData;

import java.util.List;

@KeyRoot("Destination/HTTP")
public interface HttpDestination extends KeyedData {

    static final String COMPRESSION = "Compression";
    static final String QUERY_STRING_CCSID = "QueryStringCCSID";
    static final String USE_FOLDER_MODE = "UseFolderMode";
    static final String SSL_CIPHERS = "SSLCiphers";
    static final String SSL_PROTOCOL = "SSLProtocol";
    static final String KEEP_ALIVE = "KeepAlive";
    static final String PROXY_URL = "ProxyURL";
    static final String TIMEOUT = "Timeout";
    static final String REQUEST_URL = "RequestURL";
    static final String PROXY_CONNECT_HEADERS = "ProxyConnectHeaders";
    static final String QUERY_STRING = "QueryString";
    static final String REQUEST_LINE = "RequestLine";
    static final String REQUEST_IDENTIFIER = "RequestIdentifier";
    static final String REPLY_STATUS_CODE = "ReplyStatusCode";

    @Key(COMPRESSION)
    public String getCompression() throws MbException;

    @Key(COMPRESSION)
    public void setCompression(String compression) throws MbException;

    @Key(QUERY_STRING_CCSID)
    public String getQueryStringCCSID() throws MbException;

    @Key(QUERY_STRING_CCSID)
    public void setQueryStringCCSID(int ccsid) throws MbException;

    @Key(QUERY_STRING)
    public Multimap<String, String> getQueryString() throws MbException;

    @Key(QUERY_STRING)
    public void setQueryString(Multimap<String, String> queryString) throws MbException;

    @Key(TIMEOUT)
    public void setTimeout(List<Long> timeouts) throws MbException;

    @Key(REQUEST_LINE)
    public RequestLine getRequestLine() throws MbException;

    @Key(REQUEST_URL)
    public String getRequestUrl() throws MbException;

    @Key(REQUEST_URL)
    public void setRequestUrl(String requestUrl) throws MbException;

    @Key(TIMEOUT)
    public void setTimeout(int millis) throws MbException;

    @Key(PROXY_URL)
    public void setProxyURL(String url) throws MbException;

    @Key(KEEP_ALIVE)
    public void setKeepAlive(boolean keepAlive) throws MbException;

    @Key(SSL_PROTOCOL)
    public void setSSLProtocol(String protocol) throws MbException;

    @Key(SSL_CIPHERS)
    public void setSSLCiphers(String allowedCiphers) throws MbException;

    @Key(PROXY_CONNECT_HEADERS)
    public void setProxyConnectHeaders(String header) throws MbException;

    @Key(USE_FOLDER_MODE)
    public void setUseFolderMode(boolean useFolderMode) throws MbException;

    @Key(REQUEST_IDENTIFIER)
    public void setRequestIdentifier(byte[] identifier) throws MbException;

    @Key(REQUEST_URL)
    public String getRequestURL() throws MbException;

    @Key(TIMEOUT)
    public int getTimeout() throws MbException;

    @Key(PROXY_URL)
    public String getProxyURL() throws MbException;

    @Key(KEEP_ALIVE)
    public boolean getKeepAlive() throws MbException;

    @Key(SSL_PROTOCOL)
    public String getSSLProtocol() throws MbException;

    @Key(SSL_CIPHERS)
    public String getSSLCiphers() throws MbException;

    @Key(PROXY_CONNECT_HEADERS)
    public String getProxyConnectHeaders() throws MbException;

    @Key(USE_FOLDER_MODE)
    public boolean getUseFolderMode() throws MbException;

    @Key(REQUEST_IDENTIFIER)
    public byte[] getRequestIdentifer() throws MbException;

    @Key(REPLY_STATUS_CODE)
    public int getReplyStatusCode() throws MbException;

    @Key(REPLY_STATUS_CODE)
    public void setReplyStatusCode(int replyStatusCode) throws MbException;

}
