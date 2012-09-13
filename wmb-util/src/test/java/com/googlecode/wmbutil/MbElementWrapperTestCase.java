package com.googlecode.wmbutil;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.googlecode.wmbutil.messages.DefaultMbElementWrapper;
import com.googlecode.wmbutil.messages.LocalEnvironment;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.localenvironment.DestinationFactory;
import com.googlecode.wmbutil.messages.localenvironment.destination.HttpDestination;
import com.ibm.broker.plugin.*;
import com.ibm.broker.plugin.visitor.DefaultMbMessageVisitor;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(MbMockPolicy.class)
public class MbElementWrapperTestCase {

    @Mock private MbElement element;

    private MbElementWrapper wrapper;

    @Before
    public void setUp() throws Exception {
        wrapper = new DefaultMbElementWrapper(element);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetValueWhenReadOnly() throws Exception {
        when(element.isReadOnly()).thenReturn(true);
        wrapper.setValue("value");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFieldValueWhenReadOnly() throws Exception {
        when(element.isReadOnly()).thenReturn(true);
        wrapper.setValue("field", "value");
    }

    @Test
    public void testSetValue() throws Exception {
        wrapper.setValue("field");
        verify(element).setValue("field");
    }

    @Test
    public void testSetExistingFieldValue() throws Exception {
        MbElement child = mock(MbElement.class);
        when(element.getFirstElementByPath("field")).thenReturn(child);
        wrapper.setValue("field", "value");
        verify(element).getFirstElementByPath("field");
        verify(child).setValue("value");
    }

    @Test
    public void testSetMissingFieldValue() throws Exception {
        MbElement child = mock(MbElement.class);
        when(element.getFirstElementByPath("field")).thenReturn(null);
        when(element.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "field", null)).thenReturn(child);
        wrapper.setValue("field", "value");
        verify(element).getFirstElementByPath("field");
        verify(element).createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "field", null);
        verify(child).setValue("value");
    }

    @Test
    public void testDestinationFactory() throws Exception {
        MbMessage message = new MbMessage();
        message.getRootElement().evaluateXPath("?Destination/?HTTP/?RequestURL");
        Assert.assertNotNull(DestinationFactory.HTTP.tryGet(message).orNull());
    }

    @Test
    public void testCreateDestinationFactory() throws Exception {
        MbMessage message = new MbMessage();
        Assert.assertNull(DestinationFactory.HTTP.tryGet(message).orNull());

        HttpDestination httpDestination = DestinationFactory.HTTP.getOrCreateElement(message);
        httpDestination.setRequestURL("http://a.b.c/path");

        Assert.assertNotNull(DestinationFactory.HTTP.get(message));
        Assert.assertEquals("http://a.b.c/path", DestinationFactory.HTTP.get(message).getValue("RequestURL"));
        Assert.assertEquals("http://a.b.c/path", DestinationFactory.HTTP.get(message).getRequestURL());

        httpDestination.setQueryStringCCSID(CCSID.ASCII);
        httpDestination.setQueryString(ImmutableMap.of("K1", "V1", "K2", "V2"));
        Map<String, String> qs = httpDestination.getQueryString();
        Assert.assertEquals(2, qs.size());
        Assert.assertEquals("V1", qs.get("K1"));
    }

    @Test
    public void testRemoveDestinationFactory() throws Exception {
        MbMessage message = new MbMessage();
        message.getRootElement().evaluateXPath("?Destination/?HTTP/?RequestURL");
        Assert.assertNotNull(DestinationFactory.HTTP.tryGet(message).orNull());
        DestinationFactory.HTTP.remove(message);
        Assert.assertNull(DestinationFactory.HTTP.tryGet(message).orNull());
    }

    @Test
    public void testDestination() throws Exception {
        MbMessage message = new MbMessage();
        List<MbElement> element = (List<MbElement>) message.getRootElement().evaluateXPath("?Destination/?HTTP/?RequestURL");
        element.get(0).setValue("http://a/b/c");

        Optional<HttpDestination> destination = LocalEnvironment.wrap(message).getDestination().getHTTP();

        Assert.assertTrue(destination.isPresent());
        Assert.assertEquals("http://a/b/c", destination.get().getRequestURL());
    }

    @After
    public void tearDown() throws Exception {
        PseudoNativeMbMessageManager.getInstance().accept(new DefaultMbMessageVisitor() {
            @Override public void visit(PseudoNativeMbElement element) {
                try {
                    if (element.getValue() != null)
                        System.out.println(element.getPath() + "[" + element.getValueAsString() + "]");
                    else
                        System.out.println(element.getPath());
                } catch (MbException ignored) {
                }
            }
        });
    }
}
