package com.googlecode.wmbutil;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class DateTimeTest {
    @Test
    public void testDateTime() throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
        f.setLenient(true);
        Date d = f.parse("2012-01-01T01:00:00.123333+0100");
        System.out.println(d);
    }
}
