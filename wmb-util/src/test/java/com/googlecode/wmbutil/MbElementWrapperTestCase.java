package com.googlecode.wmbutil;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.googlecode.wmbutil.messages.DefaultMbElementWrapper;
import com.googlecode.wmbutil.messages.LocalEnvironment;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.googlecode.wmbutil.messages.localenvironment.DestinationFactory;
import com.googlecode.wmbutil.messages.localenvironment.destination.HttpDestination;
import com.ibm.broker.plugin.*;
import com.ibm.broker.plugin.visitor.DefaultMbMessageVisitor;
import com.pressassociation.bus.data.KeyedProxyFactory;
import com.pressassociation.bus.messages.elements.Destination;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(MbMockPolicy.class)
public class MbElementWrapperTestCase {

    private MbMessage message;

    @Mock private MbElement element;

    private MbElementWrapper wrapper;

    @Before
    public void setUp() throws Exception {
        message = new MbMessage();
        element = spy(message.getRootElement());
        wrapper = new DefaultMbElementWrapper(element);
    }

    @Test
    public void testGenericDataParser() throws Exception {
        com.pressassociation.bus.messages.LocalEnvironment environment =
                KeyedProxyFactory.create(message, com.pressassociation.bus.messages.LocalEnvironment.class);
        Destination destination = environment.getDestination();
        destination.toString();
        destination.getHttp().setCompression("gzip");
        destination.getHttp().setQueryStringCCSID(CCSID.ASCII.getId());
        destination.getHttp().setKeepAlive(true);
        destination.getHttp().setRequestIdentifier("abcdef".getBytes());

        KeyedProxyFactory.create(message, com.pressassociation.bus.messages.elements.HttpDestination.class).setQueryString(ImmutableMultimap.of("x", "y"));
        KeyedProxyFactory.create(message, com.pressassociation.bus.messages.elements.HttpDestination.class).setQueryString(ImmutableMultimap.of("a", "b"));
        KeyedProxyFactory.create(message, com.pressassociation.bus.messages.elements.HttpDestination.class).setTimeout(ImmutableList.of(1L, 2L, 3L));

        Assert.assertNotNull(destination.getHttp().getQueryString());
        Assert.assertEquals(1, destination.getHttp().getQueryString().size());
        Assert.assertEquals("b", destination.getHttp().getQueryString().get("a").iterator().next());

        Optional<MbElement> destinationElement = destination.find();
        Assert.assertTrue(destinationElement.isPresent());

        environment.getHttpInput().setQueryString(destination.getHttp().getQueryString());
        Assert.assertTrue(environment.getHttpInput().exists());
    }

    @Test
    public void testGetValue() throws Exception {
        Assert.assertFalse(wrapper.getValue().isPresent());
        wrapper.setValue("value");
        verify(element).setValue("value");
        Assert.assertTrue(wrapper.getValue().isPresent());
        Assert.assertEquals("value", wrapper.getValue().get());
    }

    @Test
    public void testGetFieldValue() throws Exception {
        Assert.assertNotNull(wrapper.getValue("field"));
        wrapper.setValue("field", "value");
        Assert.assertNotNull(wrapper.getValue("field"));
        Assert.assertEquals("value", wrapper.getValue("field"));
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
        doReturn(child).when(element).createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "field", null);
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
        com.pressassociation.bus.messages.elements.HttpDestination httpDestination = KeyedProxyFactory.create(message, com.pressassociation.bus.messages.elements.HttpDestination.class);
//        Assert.assertNull(DestinationFactory.HTTP.tryGet(message).orNull());

//        HttpDestination httpDestination = DestinationFactory.HTTP.getOrCreateElement(message);
        httpDestination.setRequestUrl("http://a.b.c/path");

        Assert.assertNotNull(DestinationFactory.HTTP.get(message));
        Assert.assertEquals("http://a.b.c/path", DestinationFactory.HTTP.get(message).getValue("RequestURL"));
        Assert.assertEquals("http://a.b.c/path", DestinationFactory.HTTP.get(message).getRequestURL());

        httpDestination.setQueryStringCCSID(CCSID.ASCII.getId());
        httpDestination.setQueryString(ImmutableMultimap.of("K1", "V1", "K2", "V2", "K3", "V31", "K3", "V32"));
        Multimap<String, String> qs = httpDestination.getQueryString();
        Assert.assertEquals(4, qs.size());
        Collection<?> c = qs.get("K3");
        Assert.assertEquals(2, c.size());
        Assert.assertEquals("V31", c.iterator().next());
    }

    @Test
    public void testRemoveDestinationFactory() throws Exception {
        message.getRootElement().evaluateXPath("?Destination/?HTTP/?RequestURL");
        Assert.assertNotNull(DestinationFactory.HTTP.tryGet(message).orNull());
        DestinationFactory.HTTP.remove(message);
        Assert.assertNull(DestinationFactory.HTTP.tryGet(message).orNull());
    }

    @Test
    public void testDestination() throws Exception {
        @SuppressWarnings("unchecked")
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
