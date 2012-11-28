package com.pressassociation.bus;

import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Key;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.ibm.broker.plugin.MbDate;
import com.ibm.broker.plugin.MbTime;
import com.ibm.broker.plugin.MbTimestamp;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.BitSet;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
@RunWith(PowerMockRunner.class)
public class AttributeTypeTest {

    private static class Y {
        @SuppressWarnings("unchecked")
        private static ImmutableSet<Class<?>> acceptableUserDefinedAttributeTypes = ImmutableSet.<Class<?>>of(
                MbDate.class,
                MbTime.class,
                MbTimestamp.class,
                Boolean.class, boolean.class,
                byte[].class,
                String.class,
                Integer.class, int.class,
                Long.class, long.class,
                Double.class, double.class,
                BigDecimal.class,
                BitSet.class);

        private boolean initializedAttributes;

        public void ensureAttributesInitialized() {
            if (!initializedAttributes) {
                for (final Field f : getClass().getDeclaredFields()) {
                    final FieldInfo fi = FieldInfo.of(f);
                    if (f.isAnnotationPresent(Key.class)) {
                        if (Supplier.class.equals(fi.getType())) {
                            ParameterizedTypeImpl type = (ParameterizedTypeImpl) fi.getGenericType();

                            Type klass = type.getActualTypeArguments()[0];
                            if (klass instanceof Class && acceptableUserDefinedAttributeTypes.contains(klass)) {
                                fi.setValue(this, new Supplier<Object>() {
                                    @Override public Object get() {
                                        return getUserDefinedAttribute(fi.getName());
                                    }
                                });
                            } else {
                                throw new IllegalArgumentException(klass + " is not acceptable as a user defined supplied attribute type.");
                            }
                        } else if (Optional.class.equals(fi.getType())) {
                            ParameterizedTypeImpl type = (ParameterizedTypeImpl) fi.getGenericType();

                            Type klass = type.getActualTypeArguments()[0];
                            if (klass instanceof Class && acceptableUserDefinedAttributeTypes.contains(klass)) {
                                fi.setValue(this, Optional.fromNullable(getUserDefinedAttribute(fi.getName())));
                            } else {
                                throw new IllegalArgumentException(fi.getType() + " is not acceptable as a user defined attribute type.");
                            }
                        } else if (acceptableUserDefinedAttributeTypes.contains(fi.getType())) {
                            fi.setValue(this, getUserDefinedAttribute(fi.getName()));
                        } else {
                            throw new IllegalArgumentException(fi.getType() + " is not acceptable as a user defined attribute type.");
                        }
                    }
                }
                initializedAttributes = true;
            }
        }

        public Object getUserDefinedAttribute(String name) {
            return "";
        }

    }

    private static class X extends Y {
        @Key
        String stringValue;

        @Key
        Supplier<String> suppliedStringValue;

        @Key
        Optional<String> optionalStringValue;

        Supplier<String> testValueProvider;

        private X(Supplier<String> testValueProvider) {
            this.testValueProvider = testValueProvider;
        }

        @Override public Object getUserDefinedAttribute(String name) {
            return testValueProvider.get();
        }

        public void setTestValueProvider(Supplier<String> testValueProvider) {
            this.testValueProvider = testValueProvider;
        }
    }

    @Test
    public void testAttributeAquisition() throws Exception {
        X x = new X(new Supplier<String>() {
            int i = 0;

            @Override public String get() {
                return "xxx-" + ++i;
            }
        });

        x.ensureAttributesInitialized();
        Assert.assertNotNull(x.stringValue);
        Assert.assertEquals("xxx-1", x.stringValue);
        Assert.assertTrue(x.optionalStringValue.isPresent());
        Assert.assertEquals("xxx-2", x.optionalStringValue.get());
        Assert.assertNotNull(x.suppliedStringValue);
        Assert.assertEquals("xxx-3", x.suppliedStringValue.get());
    }

    @Test
    public void testNullHandling() throws Exception {
        X x = new X(new Supplier<String>() {
            @Override public String get() {
                return null;
            }
        });

        x.ensureAttributesInitialized();
        Assert.assertNull(x.stringValue);
        Assert.assertNotNull(x.optionalStringValue);
        Assert.assertFalse(x.optionalStringValue.isPresent());
        Assert.assertNotNull(x.suppliedStringValue);
        Assert.assertNull(x.suppliedStringValue.get());
    }

    @Test
    public void testValueChangeHandling() throws Exception {
        X x = new X(new Supplier<String>() {
            @Override public String get() {
                return null;
            }
        });

        x.ensureAttributesInitialized();

        x.setTestValueProvider(new Supplier<String>() {
            @Override public String get() {
                return "xxx";
            }
        });

        Assert.assertNull(x.stringValue);
        Assert.assertNotNull(x.optionalStringValue);
        Assert.assertFalse(x.optionalStringValue.isPresent());
        Assert.assertNotNull(x.suppliedStringValue);
        Assert.assertEquals("xxx", x.suppliedStringValue.get());
    }
}
