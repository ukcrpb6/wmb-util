package com.googlecode.wmbutil.messages.localenvironment.destination;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableMap;
import com.googlecode.wmbutil.CCSID;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class HttpDestination extends MbElementWrapper {

  private static final String COMPRESSION = "Compression";
  private static final String QUERY_STRING_CCSID = "QueryStringCCSID";
  private static final String USE_FOLDER_MODE = "UseFolderMode";
  private static final String SSL_CIPHERS = "SSLCiphers";
  private static final String SSL_PROTOCOL = "SSLProtocol";
  private static final String KEEP_ALIVE = "KeepAlive";
  private static final String PROXY_URL = "ProxyURL";
  private static final String TIMEOUT = "Timeout";
  private static final String REQUEST_URL = "RequestURL";
  private static final String PROXY_CONNECT_HEADERS = "ProxyConnectHeaders";
  private static final String QUERY_STRING = "QueryString";
  private static final String REQUEST_LINE = "RequestLine";

  public HttpDestination(MbElement elm) {
    super(elm);
  }

  public void setRequestURL(String url) throws MbException {
    setValue(REQUEST_URL, url);
  }

  public void setTimeout(int millis) throws MbException {
    setValue(TIMEOUT, millis);
  }

  public void setProxyURL(String url) throws MbException {
    setValue(PROXY_URL, url);
  }

  public void setRequestURI(String uri) throws MbException {
    getOrCreateField(REQUEST_LINE).setValue("RequestURI", uri);
  }

  public void setHttpVersion(String version) throws MbException {
    getOrCreateField(REQUEST_LINE).setValue("HTTPVersion", version);
  }

  public void setKeepAlive(boolean keepAlive) throws MbException {
    setValue(KEEP_ALIVE, keepAlive);
  }

  public void setMethod(String method) throws MbException {
    getOrCreateField(REQUEST_LINE).setValue("Method", method);
  }

  public void setSSLProtocol(String protocol) throws MbException {
    setValue(SSL_PROTOCOL, protocol);
  }

  public void setSSLCiphers(String allowedCiphers) throws MbException {
    setValue(SSL_CIPHERS, allowedCiphers);
  }

  public void setProxyConnectHeaders(Map<String, String> headers) throws MbException {
    setValue(PROXY_CONNECT_HEADERS,
        Joiner.on((char) 0xA).withKeyValueSeparator(": ").join(headers));
  }

  public void setUseFolderMode(boolean useFolderMode) throws MbException {
    setValue(USE_FOLDER_MODE, useFolderMode);
  }

  public void setQueryString(Map<String, String> queryString) throws MbException {
    MbElementWrapper field = getOrCreateField(QUERY_STRING);
    for (String key : queryString.keySet()) {
      field.setValue(key, queryString.get(key));
    }
  }

  public void setQueryStringCCSID(CCSID ccsid) throws MbException {
    setValue(QUERY_STRING_CCSID, ccsid.getId());
  }

  public void setCompression(String compression) throws MbException {
    setValue(COMPRESSION, compression);
  }

  public String getRequestURL() throws MbException {
    return getValue(REQUEST_URL);
  }

  public int getTimeout() throws MbException {
    return this.<Integer>getValue(TIMEOUT);
  }

  public String getProxyURL() throws MbException {
    return getValue(PROXY_URL);
  }

  public String getRequestURI() throws MbException {
    Optional<MbElementWrapper> field = getField(REQUEST_LINE);
    if(field.isPresent()) {
      return field.get().getValue("RequestURI");
    }
    return null;
  }

  public String getHttpVersion() throws MbException {
    Optional<MbElementWrapper> field = getField(REQUEST_LINE);
    if(field.isPresent()) {
      return field.get().getValue("HTTPVersion");
    }
    return null;
  }

  public boolean getKeepAlive() throws MbException {
    return this.<Boolean>getValue(KEEP_ALIVE);
  }

  public String getMethod() throws MbException {
    Optional<MbElementWrapper> field = getField(REQUEST_LINE);
    if(field.isPresent()) {
      return field.get().getValue("Method");
    }
    return null;
  }

  public String getSSLProtocol() throws MbException {
    return getValue(SSL_PROTOCOL);
  }

  public String getSSLCiphers() throws MbException {
    return getValue(SSL_CIPHERS);
  }

  public Map<String, String> getProxyConnectHeaders() throws MbException {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    String value = getValue(PROXY_CONNECT_HEADERS);
    for(String header : value.split("0x0A")) {
      String[] kv = header.split(": ");
      builder.put(kv[0], kv[1]);
    }
    return builder.build();
  }

  public boolean getUseFolderMode() throws MbException {
    return this.<Boolean>getValue(USE_FOLDER_MODE);
  }

  public Map<String, String> getQueryString() throws MbException {
    Optional<MbElementWrapper> field = getField(QUERY_STRING);
    if(field.isPresent()) {
      final MbElement firstChild = field.get().getMbElement().getFirstChild();
      if(firstChild != null) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        Iterator<MbElement> iter = new AbstractIterator<MbElement>() {
          MbElement next = firstChild;

          @Override protected MbElement computeNext() {
            if (next != null) {
              final MbElement result = next;
              try {
                next = result.getNextSibling();
              } catch (MbException ignored) {
                next = null;
              }
              return result;
            }
            return endOfData();
          }
        };

        while(iter.hasNext()) {
          MbElement child = iter.next();
          builder.put(child.getName(), child.getValueAsString());
        }
        return builder.build();
      }
    }
    return ImmutableMap.of();
  }

  public CCSID getQueryStringCCSID() throws MbException {
    return CCSID.valueOf(this.<Integer>getValue(QUERY_STRING_CCSID));
  }

  public String getCompression() throws MbException {
    return getValue(COMPRESSION);
  }

}
