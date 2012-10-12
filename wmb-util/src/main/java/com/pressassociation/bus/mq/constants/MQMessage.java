package com.pressassociation.bus.mq.constants;

import com.google.common.collect.ImmutableSet;
import com.ibm.broker.plugin.*;

import java.util.Set;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class MQMessage {
    public static final Set<String> HEADER_PARSERS = ImmutableSet.of();
    public static final Set<String> HEADER_ROOT_NAMES = ImmutableSet.of();
    public static final Set<String> BODY_PARSERS = ImmutableSet.of(
            MbMRM.PARSER_NAME,
            MbXMLNSC.PARSER_NAME, MbXMLNS.PARSER_NAME, MbXML.PARSER_NAME,
            "SOAP",
            "DataObject",
            "JMSMap", "JMSStream",
            "MIME",
            MbBLOB.PARSER_NAME,
            "IDOC",
            MbJSON.PARSER_NAME,
            "DFDL");
    public static final Set<String> BODY_ROOT_NAMES = ImmutableSet.of(
            MbMRM.ROOT_ELEMENT_NAME,
            MbXMLNSC.ROOT_ELEMENT_NAME, MbXMLNS.ROOT_ELEMENT_NAME, MbXML.ROOT_ELEMENT_NAME,
            "SOAP",
            "DataObject",
            "JMSMap", "JMSStream",
            "MIME",
            MbBLOB.ROOT_ELEMENT_NAME,
            "IDOC",
            MbJSON.ROOT_ELEMENT_NAME,
            "DFDL");
}
