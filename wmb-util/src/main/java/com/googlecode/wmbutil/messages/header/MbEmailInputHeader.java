package com.googlecode.wmbutil.messages.header;

import com.ibm.broker.plugin.MbException;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public interface MbEmailInputHeader extends MbHeader {
    String getCc() throws MbException;

    void setCc(String cc) throws MbException;

    String getFrom() throws MbException;

    void setFrom(String from) throws MbException;

    String getReplyTo() throws MbException;

    void setReplyTo(String replyTo) throws MbException;

    String getSentDate() throws MbException;

    void setSentDate(String sentDate) throws MbException;

    int getSize() throws MbException;

    void setSize(int size) throws MbException;

    String getSubject() throws MbException;

    void setSubject(String subject) throws MbException;

    String getTo() throws MbException;

    void setTo(String to) throws MbException;
}
