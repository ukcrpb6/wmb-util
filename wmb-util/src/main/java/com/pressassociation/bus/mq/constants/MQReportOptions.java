package com.pressassociation.bus.mq.constants;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@SuppressWarnings("UnusedDeclaration")
public interface MQReportOptions {
    public static final int MQRO_ACCEPT_UNSUP_IF_XMIT_MASK = 261888;
    public static final int MQRO_ACCEPT_UNSUP_MASK = -270532353;
    public static final int MQRO_ACTIVITY = 4;
    public static final int MQRO_COA = 256;
    public static final int MQRO_COA_WITH_DATA = 768;
    public static final int MQRO_COA_WITH_FULL_DATA = 1792;
    public static final int MQRO_COD = 2048;
    public static final int MQRO_COD_WITH_DATA = 6144;
    public static final int MQRO_COD_WITH_FULL_DATA = 14336;
    public static final int MQRO_COPY_MSG_ID_TO_CORREL_ID = 0;
    public static final int MQRO_DEAD_LETTER_Q = 0;
    public static final int MQRO_DISCARD_MSG = 134217728;
    public static final int MQRO_EXCEPTION = 16777216;
    public static final int MQRO_EXCEPTION_WITH_DATA = 50331648;
    public static final int MQRO_EXCEPTION_WITH_FULL_DATA = 117440512;
    public static final int MQRO_EXPIRATION = 2097152;
    public static final int MQRO_EXPIRATION_WITH_DATA = 6291456;
    public static final int MQRO_EXPIRATION_WITH_FULL_DATA = 14680064;
    public static final int MQRO_NEW_MSG_ID = 0;
    public static final int MQRO_NONE = 0;
    public static final int MQRO_PASS_CORREL_ID = 64;
    public static final int MQRO_PASS_DISCARD_AND_EXPIRY = 16384;
    public static final int MQRO_PASS_MSG_ID = 128;
    public static final int MQRO_REJECT_UNSUP_MASK = 270270464;
}