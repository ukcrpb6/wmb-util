package com.googlecode.wmbutil.messages.header;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import java.util.Date;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class MbPropertiesHeaderImpl extends AbstractMbHeader implements MbPropertiesHeader {

    MbPropertiesHeaderImpl(MbElement elm) throws MbException {
        super(elm, MbHeaderType.PROPERTIES);
    }

    @Override public int getCodedCharSetId() throws MbException {
        return this.<Integer>getValue(CodedCharSetId);
    }

    @Override public Date getCreationTime() throws MbException {
        return getValue(CreationTime);
    }

    @Override public String getContentType() throws MbException {
        return getValue(ContentType);
    }

    @Override public int getEncoding() throws MbException {
        return this.<Integer>getValue(Encoding);
    }

    @Override public Date getExpirationTime() throws MbException {
        return getValue(ExpirationTime);
    }

    @Override public String getIdentityMappedIssuedBy() throws MbException {
        return getValue(IdentityMappedIssuedBy);
    }

    @Override public String getIdentityMappedPassword() throws MbException {
        return getValue(IdentityMappedPassword);
    }

    @Override public String getIdentityMappedToken() throws MbException {
        return getValue(IdentityMappedToken);
    }

    @Override public String getIdentityMappedType() throws MbException {
        return getValue(IdentityMappedType);
    }

    @Override public String getIdentitySourceIssuedBy() throws MbException {
        return getValue(IdentitySourceIssuedBy);
    }

    @Override public String getIdentitySourcePassword() throws MbException {
        return getValue(IdentitySourcePassword);
    }

    @Override public String getIdentitySourceToken() throws MbException {
        return getValue(IdentitySourceToken);
    }

    @Override public String getIdentitySourceType() throws MbException {
        return getValue(IdentitySourceType);
    }

    @Override
    public String getMessageFormat() throws MbException {
        return getValue(MessageFormat);
    }

    @Override
    public void setMessageFormat(String messageFormat) throws MbException {
        setValue(MessageFormat, messageFormat);
    }

    @Override
    public String getMessageSet() throws MbException {
        return getValue(MessageSet);
    }

    @Override
    public void setMessageSet(String messageSet) throws MbException {
        setValue(MessageSet, messageSet);
    }

    @Override
    public String getMessageType() throws MbException {
        return getValue(MessageType);
    }

    @Override public boolean getPersistence() throws MbException {
        return this.<Boolean>getValue(Persistence);
    }

    @Override public int getPriority() throws MbException {
        return this.<Integer>getValue(Priority);
    }

    @Override public byte[] getReplyIdentifier() throws MbException {
        return getValue(ReplyIdentifier);
    }

    @Override public String getReplyProtocol() throws MbException {
        return getValue(ReplyProtocol);
    }

    @Override
    public void setMessageType(String messageType) throws MbException {
        setValue(MessageType, messageType);
    }

    @Override public void setPersistence(boolean value) throws MbException {
        setValue(Persistence, value);
    }

    @Override public void setPriority(int value) throws MbException {
        setValue(Priority, value);
    }

    @Override public void setReplyIdentifier(byte[] value) throws MbException {
        setValue(ReplyIdentifier, value);
    }

    @Override public void setReplyProtocol(String value) throws MbException {
        setValue(ReplyProtocol, value);
    }

    @Override
    public String getTopic() throws MbException {
        return getValue(Topic);
    }

    @Override public boolean getTransactional() throws MbException {
        return this.<Boolean>getValue(Transactional);
    }

    @Override public void setCodedCharSetId(int value) throws MbException {
        setValue(CodedCharSetId, value);
    }

    @Override public void setCreationTime(Date value) throws MbException {
        setValue(CreationTime, value);
    }

    @Override public void setContentType(String value) throws MbException {
        setValue(ContentType, value);
    }

    @Override public void setEncoding(int value) throws MbException {
        setValue(Encoding, value);
    }

    @Override public void setExpirationTime(Date value) throws MbException {
        setValue(ExpirationTime, value);
    }

    @Override public void setIdentityMappedIssuedBy(String value) throws MbException {
        setValue(IdentityMappedIssuedBy, value);
    }

    @Override public void setIdentityMappedPassword(String value) throws MbException {
        setValue(IdentityMappedPassword, value);
    }

    @Override public void setIdentityMappedToken(String value) throws MbException {
        setValue(IdentityMappedToken, value);
    }

    @Override public void setIdentityMappedType(String value) throws MbException {
        setValue(IdentityMappedType, value);
    }

    @Override public void setIdentitySourceIssuedBy(String value) throws MbException {
        setValue(IdentitySourceIssuedBy, value);
    }

    @Override public void setIdentitySourcePassword(String value) throws MbException {
        setValue(IdentitySourcePassword, value);
    }

    @Override public void setIdentitySourceToken(String value) throws MbException {
        setValue(IdentitySourceToken, value);
    }

    @Override public void setIdentitySourceType(String value) throws MbException {
        setValue(IdentitySourceType, value);
    }

    @Override
    public void setTopic(String topic) throws MbException {
        setValue(Topic, topic);
    }

    @Override public void setTransactional(boolean value) throws MbException {
        setValue(Transactional, value);
    }
}
