package com.pressassociation.bus.data;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbXPath;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeyedProxy {

    public static final String RELATIVE_SELF = ".";

    private static interface XPathable {
        public Object evaluateXPath(String s) throws com.ibm.broker.plugin.MbException;

        public Object evaluateXPath(MbXPath mbXPath) throws com.ibm.broker.plugin.MbException;
    }

    private static class ForwardingMbMessageXPathable implements XPathable {
        private final MbMessage message;

        public ForwardingMbMessageXPathable(MbMessage message) {
            this.message = message;
        }

        @Override
        public Object evaluateXPath(String s) throws MbException {
            return message.evaluateXPath(s);
        }

        @Override
        public Object evaluateXPath(MbXPath mbXPath) throws MbException {
            return message.evaluateXPath(mbXPath);
        }
    }

    private static class ForwardingMbElementXPathable implements XPathable {
        private final MbElement element;

        public ForwardingMbElementXPathable(MbElement element) {
            this.element = element;
        }

        @Override
        public Object evaluateXPath(String s) throws MbException {
            return element.evaluateXPath(s);
        }

        @Override
        public Object evaluateXPath(MbXPath mbXPath) throws MbException {
            return element.evaluateXPath(mbXPath);
        }
    }

    public static <T extends KeyedData> T createProxy(final MbMessage message, final Class<T> proxyClass) throws MbException {
        KeyRoot root = proxyClass.getAnnotation(KeyRoot.class);
        if (root == null) {
            return createProxy(new ForwardingMbMessageXPathable(message), RELATIVE_SELF, proxyClass);
        }

        @SuppressWarnings("unchecked")
        List<MbElement> elements = (List<MbElement>) message.getRootElement().evaluateXPath(root.value());

        /*
         * Message element exists, therefore make that element the relative root
         */
        if (!elements.isEmpty()) {
            return createProxy(new ForwardingMbElementXPathable(elements.get(0)), RELATIVE_SELF, proxyClass);
        }

        /*
         * Message element does not exist, use message body as relative root
         */
        return createProxy(new ForwardingMbMessageXPathable(message), root.value(), proxyClass);
    }

    public static String buildPath(String parent, String child) {
        if (parent == null || "".equals(parent)) {
            return child;
        }
        return parent + "/" + child;
    }

    public static String buildCreatePath(String parent, String child) {
        return buildPath(parent, child).replace("/", "/?");
    }

    private static <T extends KeyedData> T createProxy(final XPathable root, final String relativePath, final Class<T> proxyClass) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{proxyClass}, new InvocationHandler() {

            /**
             * Proxy handler for setter method.
             *
             * @param o Object method is being invoked upon
             * @param method Method being invoked
             * @param objects Arguments to method
             * @param methodKey Key annotation on method
             * @return void
             * @throws MbException
             */
            private Object handleSetter(Object o, Method method, Object[] objects, Key methodKey) throws MbException {
                if (Collection.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    @SuppressWarnings("unchecked")
                    List<MbElement> elements = (List<MbElement>) root.evaluateXPath(buildPath(relativePath, methodKey.value()));
                    for (MbElement e : elements) {
                        e.detach();
                    }
                    @SuppressWarnings("unchecked") List<MbElement> parentList = (List<MbElement>) root.evaluateXPath(relativePath.replace("/", "/?"));
                    MbElement parent = parentList.get(0);
                    for (Object obj : (Collection<?>) objects[0]) {
                        parent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, methodKey.value(), obj);
                    }
                } else if (Map.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    @SuppressWarnings("unchecked")
                    List<MbElement> elements = (List<MbElement>) root.evaluateXPath(buildPath(relativePath, methodKey.value()));
                    if (!elements.isEmpty()) {
                        MbElement parent = elements.get(0);
                        while (parent.getLastChild() != null) {
                            parent.getLastChild().detach();
                        }
                    }

                    @SuppressWarnings("unchecked") List<MbElement> parentList = (List<MbElement>) root.evaluateXPath(
                            buildCreatePath(relativePath, methodKey.value()));
                    MbElement aParent = parentList.get(0);
                    //noinspection unchecked
                    for (Map.Entry<String, ?> obj : ((Map<String, ?>) objects[0]).entrySet()) {
                        aParent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, obj.getKey(), obj.getValue());
                    }
                } else {
                    @SuppressWarnings("unchecked")
                    List<MbElement> elements = (List<MbElement>) root.evaluateXPath(buildPath(relativePath, methodKey.value()).replace("/", "/?"));

                    MbElement firstElement = elements.get(0);
                    firstElement.setValue(objects[0]);
                }
                return null;
            }

            private Object handleGetter(Object o, Method method, Object[] objects, Key methodKey) throws MbException {
                if(Collection.class.isAssignableFrom(method.getReturnType())) {
                    List<MbElement> result = (List<MbElement>) root.evaluateXPath(relativePath + "/" + methodKey.value());
                    ImmutableList.Builder<Object> builder = ImmutableList.builder();
                    for(MbElement child : result) {
                        builder.add(child.getValue());
                    }
                    return builder.build();
                }
                if(Map.class.isAssignableFrom(method.getReturnType())) {
                    List<MbElement> result = (List<MbElement>) root.evaluateXPath(relativePath + "/" + methodKey.value());
                    MbElement parent = result.get(0);
                    MbElement child = parent.getFirstChild();

                    ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
                    while(child != null) {
                        if(child.getType() == MbElement.TYPE_NAME_VALUE) {
                            builder.put(child.getName(), child.getValue());
                        }
                        child = child.getNextSibling();
                    }
                    return builder.build();
                }

                Object result = root.evaluateXPath(relativePath + "/" + methodKey.value());
                if(method.getReturnType().isAssignableFrom(result.getClass())) {
                    return result;
                }
                throw new ClassCastException(
                        "Return type is " + result.getClass() + " but expected " + method.getReturnType());
            }

            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if ("exists".equals(method.getName())) {
                    return !((List<?>) root.evaluateXPath(relativePath)).isEmpty();
                }

                Key methodKey = method.getAnnotation(Key.class);
                if (methodKey != null) {
                    if (KeyedData.class.isAssignableFrom(method.getReturnType())) {
                        //noinspection unchecked
                        return createProxy(root, buildPath(relativePath, methodKey.value()), (Class<? extends KeyedData>) method.getReturnType());
                    } else {
                        /*
                         * Handle Setter
                         */
                        if (method.getName().startsWith("set")) {
                            return handleSetter(o, method, objects, methodKey);
                        }
                        /*
                         * Handle Getter
                         */
                        if (method.getName().startsWith("get")) {
                            return handleGetter(o, method, objects, methodKey);
                        }
                        throw new UnsupportedOperationException();
                    }
                }
                if ("toString".equals(method.getName())) {
                    return Objects.toStringHelper(o).add("interface", proxyClass.getName()).toString();
                }
                throw new UnsupportedOperationException();
            }
        });
    }
}