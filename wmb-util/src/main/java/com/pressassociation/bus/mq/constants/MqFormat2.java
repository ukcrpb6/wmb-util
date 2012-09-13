package com.pressassociation.bus.mq.constants;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@SuppressWarnings("UnusedDeclaration")
public interface MQFormat {
    public static final String MQFMT_NONE = "        ";
    public static final String MQFMT_ADMIN = "MQADMIN ";
    public static final String MQFMT_CHANNEL_COMPLETED = "MQCHCOM ";
    public static final String MQFMT_COMMAND_1 = "MQCMD1  ";
    public static final String MQFMT_COMMAND_2 = "MQCMD2  ";
    public static final String MQFMT_DEAD_LETTER_HEADER = "MQDEAD  ";
    public static final String MQFMT_EVENT = "MQEVENT ";
    public static final String MQFMT_PCF = "MQPCF   ";
    public static final String MQFMT_STRING = "MQSTR   ";
    public static final String MQFMT_TRIGGER = "MQTRIG  ";
    public static final String MQFMT_XMIT_Q_HEADER = "MQXMIT  ";
    public static final String MQFMT_CICS = "MQCICS  ";
    public static final String MQFMT_IMS = "MQIMS   ";
    public static final String MQFMT_IMS_VAR_STRING = "MQIMSVS ";
    public static final String MQFMT_RF_HEADER = "MQHRF   ";
    public static final String MQFMT_RF_HEADER_1 = "MQHRF   ";
    public static final String MQFMT_RF_HEADER_2 = "MQHRF2  ";
    public static final String MQFMT_DIST_HEADER = "MQHDIST ";
    public static final String MQFMT_MD_EXTENSION = "MQHMDE  ";
    public static final String MQFMT_REF_MSG_HEADER = "MQHREF  ";
}
