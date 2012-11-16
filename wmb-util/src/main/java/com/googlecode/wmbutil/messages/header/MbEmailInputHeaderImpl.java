package com.googlecode.wmbutil.messages.header;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class MbEmailInputHeaderImpl extends AbstractMbHeader implements MbEmailInputHeader {

    public static final String FIELD_TO = "To";
    public static final String FIELD_FROM = "From";
    public static final String FIELD_CC = "Cc";
    public static final String FIELD_REPLY_TO = "ReplyTo";
    public static final String FIELD_SUBJECT = "Subject";
    public static final String FIELD_SIZE = "Size";
    public static final String FIELD_SENT_DATE = "SentDate";

    /**
     * Constructor defining the header with the specific element.
     *
     * @param elm The message element
     * @throws com.ibm.broker.plugin.MbException
     *
     */
    public MbEmailInputHeaderImpl(MbElement elm) throws MbException {
        super(elm, MbHeaderType.EMAIL);
    }

    @Override public String getCc() throws MbException {
        return getValue(FIELD_CC);
    }

    @Override public void setCc(String cc) throws MbException {
        setValue(FIELD_CC, cc);
    }

    @Override public String getFrom() throws MbException {
        return getValue(FIELD_FROM);
    }

    @Override public void setFrom(String from) throws MbException {
        setValue(FIELD_FROM, from);
    }

    @Override public String getReplyTo() throws MbException {
        return getValue(FIELD_REPLY_TO);
    }

    @Override public void setReplyTo(String replyTo) throws MbException {
        setValue(FIELD_REPLY_TO, replyTo);
    }

    @Override public String getSentDate() throws MbException {
        return getValue(FIELD_SENT_DATE);
    }

    @Override public void setSentDate(String sentDate) throws MbException {
        setValue(FIELD_SENT_DATE, sentDate);
    }

    @Override public int getSize() throws MbException {
        return this.<Integer>getValue(FIELD_SIZE);
    }

    @Override public void setSize(int size) throws MbException {
        setValue(FIELD_SIZE, size);
    }

    @Override public String getSubject() throws MbException {
        return getValue(FIELD_SUBJECT);
    }

    @Override public void setSubject(String subject) throws MbException {
        setValue(FIELD_SUBJECT, subject);
    }

    @Override public String getTo() throws MbException {
        return getValue(FIELD_TO);
    }

    @Override public void setTo(String to) throws MbException {
        setValue(FIELD_TO, to);
    }
}
