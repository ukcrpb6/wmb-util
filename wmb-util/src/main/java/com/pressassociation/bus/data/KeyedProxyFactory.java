package com.pressassociation.bus.data;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.ibm.broker.plugin.*;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeyedProxyFactory {

    public static final String RELATIVE_SELF = ".";
    public static final String EMPTY_PATH = "";

    private static boolean isAbsolutePath(String path) {
        return path.charAt(0) == '/';
    }

    public static <T extends KeyedData> T create(final MbMessage message, final Class<T> proxyClass) throws MbException {
        KeyRoot root = proxyClass.getAnnotation(KeyRoot.class);
        if (root == null) {
            return createProxy(new ForwardingMbElementXPathable(message, message.getRootElement()), EMPTY_PATH, proxyClass);
        }

        @SuppressWarnings("unchecked")
        List<MbElement> elements = (List<MbElement>) message.getRootElement().evaluateXPath(root.value());

        /*
         * Message element exists, therefore make that element the relative root
         */
        if (!elements.isEmpty()) {
            return createProxy(new ForwardingMbElementXPathable(message, elements.get(0)), EMPTY_PATH, proxyClass);
        }

        if (isAbsolutePath(root.value())) {
            return createProxy(new ForwardingMbElementXPathable(message, message.getRootElement()), root.value(), proxyClass);
        }

        /*
         * Message element does not exist, use message body as relative root
         */
        return createProxy(new ForwardingMbElementXPathable(message, message.getRootElement()), root.value(), proxyClass);
    }

    /**
     * Create the xpath for a parent and it's child.
     *
     * @param parent The parent
     * @param child The child
     * @return String path
     */
    private static String buildPath(@Nullable String parent, String child) {
        if (parent == null || "".equals(parent)) {
            return child;
        }
        return parent + "/" + child;
    }

    /**
     * Create the xpath for a parent and it's child, where nodes will be created if missing.
     * @param parent
     * @param child
     * @return
     */
    private static String buildCreatePath(String parent, String child) {
        final String path = buildPath(parent, child);
        return ((path.charAt(0) == '/' || path.charAt(0) == '.') ? "" : "?") + path.replace("/", "/?");
    }

    // TODO: Make relativePath an Optional element
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

                    MbElement parent;
                    if (relativePath.length() > 0) {
                        @SuppressWarnings("unchecked") List<MbElement> parentList = (List<MbElement>) root.evaluateXPath(relativePath.replace("/", "/?"));
                        parent = parentList.get(0);
                    } else {
                        parent = root.getRootElement();
                    }

                    for (Object obj : (Collection<?>) objects[0]) {
                        parent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, methodKey.value(), obj);
                    }
                } else if (Multimap.class.isAssignableFrom(method.getParameterTypes()[0])) {
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
                    for (Map.Entry<String, ?> obj : ((Multimap<String, ?>) objects[0]).entries()) {
                        aParent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, obj.getKey(), obj.getValue());
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
                        Object value = obj.getValue();
                        if (value.getClass().isArray()) {
                            for (Object o1 : ((Object[]) value)) {
                                aParent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, obj.getKey(), o1);
                            }
                        } else if (Collection.class.isAssignableFrom(value.getClass())) {
                            for (Object o1 : ((Collection<?>) value)) {
                                aParent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, obj.getKey(), o1);
                            }
                        } else {
                            aParent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, obj.getKey(), value);
                        }
                    }
                } else {
                    final String path = buildCreatePath(relativePath, methodKey.value());
                    @SuppressWarnings("unchecked")
                    List<MbElement> elements = (List<MbElement>) root.evaluateXPath(path);

                    MbElement firstElement = elements.get(0);
                    firstElement.setValue(objects[0]);
                }
                return null;
            }

            @SuppressWarnings("unchecked")
            private List<MbElement> evaluate(Key methodKey) throws MbException {
                return (List<MbElement>) root.evaluateXPath(buildPath(relativePath, methodKey.value()));
            }

            private Object handleGetter(Object o, Method method, Object[] objects, Key methodKey) throws MbException {
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    List<MbElement> result = evaluate(methodKey);
                    ImmutableList.Builder<Object> builder = ImmutableList.builder();
                    for (MbElement child : result) {
                        builder.add(child.getValue());
                    }
                    return builder.build();
                }
                if (Multimap.class.isAssignableFrom(method.getReturnType())) {
                    MbElement parent = evaluate(methodKey).get(0);
                    MbElement child = parent.getFirstChild();

                    ImmutableListMultimap.Builder<Object, Object> builder = ImmutableListMultimap.builder();
                    while (child != null) {
                        if (child.getType() == MbElement.TYPE_NAME_VALUE) {
                            builder.put(child.getName(), child.getValue());
                        }
                        child = child.getNextSibling();
                    }
                    return builder.build();
                }
                if (Map.class.isAssignableFrom(method.getReturnType())) {
                    MbElement parent = evaluate(methodKey).get(0);
                    MbElement child = parent.getFirstChild();

                    Map<String, Object> builder = Maps.newHashMap();
                    while (child != null) {
                        if (child.getType() == MbElement.TYPE_NAME_VALUE) {
                            builder.put(child.getName(), child.getValue());
                        }
                        child = child.getNextSibling();
                    }
                    return ImmutableMap.copyOf(builder);
                }

                List<MbElement> result = evaluate(methodKey);
                if (!result.isEmpty()) {
                    MbElement value = result.get(0);
                    if (method.getReturnType().isAssignableFrom(value.getValue().getClass())) {
                        return value.getValue();
                    }
                    if (method.getReturnType().isAssignableFrom(String.class)) {
                        return value.getValueAsString();
                    }
                } else {
                    throw new NoSuchFieldError("Field not found: " + buildPath(relativePath, methodKey.value()));
                }
                throw new ClassCastException(
                        "Return type is " + result.getClass() + " but expected " + method.getReturnType());
            }

            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if ("exists".equals(method.getName())) {
                    return relativePath.length() == 0 || !((List<?>) root.evaluateXPath(relativePath)).isEmpty();
                }

                if ("get".equals(method.getName())) {
                    if (relativePath.length() > 0) {
                        //noinspection unchecked
                        List<MbElement> elements = ((List<MbElement>) root.evaluateXPath(relativePath));
                        return elements.isEmpty() ? Optional.<MbElement>absent() : Optional.of(elements.get(0));
                    } else {
                        return root.getRootElement();
                    }
                }

                if ("find".equals(method.getName())) {
                    if (relativePath.length() > 0) {
                        //noinspection unchecked
                        List<MbElement> elements = ((List<MbElement>) root.evaluateXPath(relativePath));
                        return elements.isEmpty() ? Optional.<MbElement>absent() : Optional.of(elements.get(0));
                    } else {
                        return root.getRootElement();
                    }
                }

                if ("findAll".equals(method.getName())) {
                    if (relativePath.length() > 0) {
                        return root.evaluateXPath(relativePath);
                    } else {
                        // TODO: findAll for rooted elements
                        throw new UnsupportedOperationException();
                    }
                }

                Key methodKey = method.getAnnotation(Key.class);
                if (methodKey != null) {
                    // TODO: Ensure annotated method throws exception
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
                if (KeyedData.class.isAssignableFrom(method.getReturnType()) && method.getReturnType().isAnnotationPresent(KeyRoot.class)) {
                    //noinspection unchecked
                    return create(root.getMessage(), (Class<? extends KeyedData>) method.getReturnType());
                }
                if ("toString".equals(method.getName())) {
                    return Objects.toStringHelper(o).add("interface", proxyClass.getName()).toString();
                }
                throw new UnsupportedOperationException();
            }
        });
    }

    @Beta
    public <T extends KeyedData> T configureLocalEnvironment(MbMessageAssembly assembly, Configuration<T> configuration,
   			Class<T> dataClass) throws MbException {
   		return configure(KeyedProxyFactory.create(assembly.getLocalEnvironment(), dataClass), configuration);
   	}

    @Beta
   	public <T extends KeyedData> T configure(MbMessage message, Configuration<T> configuration,
   			Class<T> dataClass) throws MbException {
   		return configure(KeyedProxyFactory.create(message, dataClass), configuration);
   	}

    @Beta
   	public <T extends KeyedData> T configure(T configurable, Configuration<T> configuration) throws MbException {
   		configuration.apply(configurable);
   		return configurable;
   	}

    @Beta
   	private static interface Configuration<T extends KeyedData> {
   		public void apply(T object) throws MbException;
   	}

    @SuppressWarnings("UnusedDeclaration")
    private static interface XPathable {

        public Object evaluateXPath(String s) throws MbException;

        public Object evaluateXPath(MbXPath mbXPath) throws MbException;

        public MbElement getRootElement() throws MbException;

        public MbMessage getMessage() throws MbException;
    }

    @SuppressWarnings("UnusedDeclaration")
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

        @Override public MbElement getRootElement() throws MbException {
            return message.getRootElement();
        }

        @Override public MbMessage getMessage() throws MbException {
            return message;
        }
    }

    private static class ForwardingMbElementXPathable implements XPathable {
        private final MbMessage message;
        private final MbElement element;

        public ForwardingMbElementXPathable(MbMessage message, MbElement element) {
            this.message = message;
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

        @Override public MbElement getRootElement() throws MbException {
            return element;
        }

        @Override public MbMessage getMessage() throws MbException {
            return message;
        }

    }
}