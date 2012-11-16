package com.ibm.broker.javacompute;

import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Key;
import com.google.common.collect.ImmutableSet;
import com.ibm.broker.plugin.MbDate;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbTime;
import com.ibm.broker.plugin.MbTimestamp;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.BitSet;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class AbstractConfiguredJavaComputeNode extends MbJavaComputeNode {
    private boolean initializedAttributes;

    @SuppressWarnings("unchecked")
    private static ImmutableSet<Class<? extends Serializable>> acceptableUserDefinedAttributeTypes = ImmutableSet.of(
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
                    if (acceptableUserDefinedAttributeTypes.contains(fi.getType())) {
                        fi.setValue(this, getUserDefinedAttribute(fi.getName()));
                    } else {
                        throw new IllegalArgumentException(fi.getType() + " is not acceptable as a user defined attribute type.");
                    }
                }
            }
            initializedAttributes = true;
        }
    }
}
