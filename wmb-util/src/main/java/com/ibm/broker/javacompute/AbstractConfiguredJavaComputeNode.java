package com.ibm.broker.javacompute;

import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Key;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.ibm.broker.plugin.MbDate;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbTime;
import com.ibm.broker.plugin.MbTimestamp;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.BitSet;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class AbstractConfiguredJavaComputeNode extends MbJavaComputeNode {
    private boolean initializedAttributes;

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

    @Override public void onInitialize() throws MbException {
        ensureAttributesInitialized();
    }

    protected void ensureAttributesInitialized() {
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
                            throw new IllegalArgumentException(klass + " is not acceptable as an user defined supplied attribute type.");
                        }
                    } else if (Optional.class.equals(fi.getType())) {
                        ParameterizedTypeImpl type = (ParameterizedTypeImpl) fi.getGenericType();

                        Type klass = type.getActualTypeArguments()[0];
                        if (klass instanceof Class && acceptableUserDefinedAttributeTypes.contains(klass)) {
                            fi.setValue(this, Optional.fromNullable(getUserDefinedAttribute(fi.getName())));
                        } else {
                            throw new IllegalArgumentException(fi.getType() + " is not acceptable as an user defined optional attribute type.");
                        }
                    } else if (acceptableUserDefinedAttributeTypes.contains(fi.getType())) {
                        fi.setValue(this, getUserDefinedAttribute(fi.getName()));
                    } else {
                        throw new IllegalArgumentException(fi.getType() + " is not acceptable as an user defined attribute type.");
                    }
                }
            }
            initializedAttributes = true;
        }
    }
}
