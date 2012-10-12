package com.googlecode.wmbutil.messages.header;

import com.ibm.broker.plugin.MbException;

import java.util.Date;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public interface MbPropertiesHeader extends MbHeader {

    @Deprecated public static final String MSG_SET = "MessageSet";

    @Deprecated public static final String MSG_TYPE = "MessageType";

    @Deprecated public static final String FORMAT = "MessageFormat";

    @Deprecated public static final String TOPIC = "Topic";

    public static final String CodedCharSetId = "CodedCharSetId";

    public static final String CreationTime = "CreationTime";

    public static final String ContentType = "ContentType";

    public static final String Encoding = "Encoding";

    public static final String ExpirationTime = "ExpirationTime";

    public static final String IdentityMappedIssuedBy = "IdentityMappedIssuedBy";

    public static final String IdentityMappedPassword = "IdentityMappedPassword";

    public static final String IdentityMappedToken = "IdentityMappedToken";

    public static final String IdentityMappedType = "IdentityMappedType";

    public static final String IdentitySourceIssuedBy = "IdentitySourceIssuedBy";

    public static final String IdentitySourcePassword = "IdentitySourcePassword";

    public static final String IdentitySourceToken = "IdentitySourceToken";

    public static final String IdentitySourceType = "IdentitySourceType";

    public static final String MessageFormat = "MessageFormat";

    public static final String MessageSet = "MessageSet";

    public static final String MessageType = "MessageType";

    public static final String Persistence = "Persistence";

    public static final String Priority = "Priority";

    public static final String ReplyIdentifier = "ReplyIdentifier";

    public static final String ReplyProtocol = "ReplyProtocol";

    public static final String Topic = "Topic";

    public static final String Transactional = "Transactional";

    int getCodedCharSetId() throws MbException;

    Date getCreationTime() throws MbException;

    String getContentType() throws MbException;

    int getEncoding() throws MbException;

    Date getExpirationTime() throws MbException;

    String getIdentityMappedIssuedBy() throws MbException;

    String getIdentityMappedPassword() throws MbException;

    String getIdentityMappedToken() throws MbException;

    String getIdentityMappedType() throws MbException;

    String getIdentitySourceIssuedBy() throws MbException;

    String getIdentitySourcePassword() throws MbException;

    String getIdentitySourceToken() throws MbException;

    String getIdentitySourceType() throws MbException;

    String getMessageFormat() throws MbException;

    String getMessageSet() throws MbException;

    String getMessageType() throws MbException;

    boolean getPersistence() throws MbException;

    int getPriority() throws MbException;

    byte[] getReplyIdentifier() throws MbException;

    String getReplyProtocol() throws MbException;

    String getTopic() throws MbException;

    boolean getTransactional() throws MbException;

    void setCodedCharSetId(int value) throws MbException;

    void setCreationTime(Date value) throws MbException;

    void setContentType(String value) throws MbException;

    void setEncoding(int value) throws MbException;

    void setExpirationTime(Date value) throws MbException;

    void setIdentityMappedIssuedBy(String value) throws MbException;

    void setIdentityMappedPassword(String value) throws MbException;

    void setIdentityMappedToken(String value) throws MbException;

    void setIdentityMappedType(String value) throws MbException;

    void setIdentitySourceIssuedBy(String value) throws MbException;

    void setIdentitySourcePassword(String value) throws MbException;

    void setIdentitySourceToken(String value) throws MbException;

    void setIdentitySourceType(String value) throws MbException;

    void setMessageFormat(String value) throws MbException;

    void setMessageSet(String value) throws MbException;

    void setMessageType(String value) throws MbException;

    void setPersistence(boolean value) throws MbException;

    void setPriority(int value) throws MbException;

    void setReplyIdentifier(byte[] value) throws MbException;

    void setReplyProtocol(String value) throws MbException;

    void setTopic(String value) throws MbException;

    void setTransactional(boolean value) throws MbException;

}
