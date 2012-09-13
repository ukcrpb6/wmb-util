package com.googlecode.wmbutil.messages;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.googlecode.wmbutil.NiceMbException;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class MbElementWrapper {

    private MbElement wrappedElm;

    protected MbElementWrapper(MbElement elm) {
        this.wrappedElm = elm;
    }

    public MbElement getMbElement() {
        return wrappedElm;
    }

    public boolean isReadOnly() {
        return getMbElement().isReadOnly();
    }

    private Constructor<? extends MbElementWrapper> getConstructor(Class<? extends MbElementWrapper> adapterClazz) {
        try {
            return adapterClazz.getConstructor(MbElement.class);
        } catch (NoSuchMethodException e) {
            throw Throwables.propagate(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends MbElementWrapper> T getAdapter(Object adaptable, Class<T> adapterType) {
        try {
            return (T) getConstructor(adapterType).newInstance(adaptable);
        } catch (InstantiationException e) {
            throw Throwables.propagate(e);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        } catch (InvocationTargetException e) {
            throw Throwables.propagate(e);
        }
    }

    // Candidate for lazy loading
    public DefaultMbElementWrapper getField(String field) throws MbException {
        return getField(field, DefaultMbElementWrapper.class);
    }

    // Candidate for lazy loading
    public <T extends MbElementWrapper> T getField(String field, final Class<T> adapterType) throws MbException {
        Optional<T> f = tryGetField(field, adapterType);
        if(f.isPresent()) {
            return f.get();
        }
        throw new NoSuchElementException("Could not find field [" + field + "]");
    }

    // Candidate for lazy loading
    public Optional<DefaultMbElementWrapper> tryGetField(String field) throws MbException {
        return tryGetField(field, DefaultMbElementWrapper.class);
    }

    // Candidate for lazy loading
    public <T extends MbElementWrapper> Optional<T> tryGetField(
            String field, final Class<T> adapterType) throws MbException {
      return Optional.fromNullable(getMbElement().getFirstElementByPath(field)).transform(
          new Function<MbElement, T>() {
            @Override public T apply(MbElement input) {
              return getAdapter(input, adapterType);
            }
          });
    }

    public MbElementWrapper getOrCreateField(String field) throws MbException {
      return getOrCreateField(field, DefaultMbElementWrapper.class);
    }

    public <T extends MbElementWrapper> T getOrCreateField(String field, Class<T> adapterType) throws MbException {
      Optional<T> adapter = tryGetField(field, adapterType);
      if(adapter.isPresent()) {
        return adapter.get();
      }
      return getAdapter(getMbElement().createElementAsLastChild(MbElement.TYPE_UNKNOWN, field, null), adapterType);
    }

    public <T> T getValue(String field) throws MbException {
        MbElement elm = getMbElement().getFirstElementByPath(field);
        if (elm != null) {
            //noinspection unchecked
            return (T) elm.getValue();
        } else {
            throw new NiceMbException("Property not found " + field);
        }
    }

    /**
     * Gets the value
     *
     * @return T representation of object value
     * @throws MbException
     */
    public <T> Optional<T> getValue() throws MbException {
        //noinspection unchecked
        return Optional.fromNullable((T) getMbElement().getValue());
    }

    /**
     * Sets the value
     *
     * @param value Element value
     * @throws MbException
     */
    public <T> void setValue(T value) throws MbException {
        checkState(!isReadOnly(), "MbElement is immutable");
        // Set value on MbElement
        getMbElement().setValue(value);
    }

    public <T> void setValue(String field, T value) throws MbException {
        checkState(!isReadOnly(), "MbElement is immutable");
        // Set value on MbElement
        MbElement elm = getMbElement().getFirstElementByPath(field);
        if (elm == null) {
            elm = getMbElement().createElementAsLastChild(MbElement.TYPE_NAME_VALUE, field, null);
        }
        elm.setValue(value);
    }

    /**
     * Sets a fixed length padded value on the specified field
     *
     * @param field The field to be set
     * @param value The value to be set
     * @param length The fixed length of the String value
     * @throws MbException Invalid length for fixed length string
     */
    protected void setFixedStringValue(String field, String value, int length) throws MbException {
        checkState(!isReadOnly(), "MbElement is immutable");
        // Set value on MbElement
        if (value.length() > length) {
            throw new NiceMbException("Value for field '%s' to long, max length is %d", field, length);
        } else {
            setValue(field, Strings.padEnd(value, length, ' '));
        }
    }

    protected void setFixedByteArrayValue(String field, byte[] value, int length) throws MbException {
        checkState(!isReadOnly());
        // Set value on MbElement
        byte[] b;
        if (value.length > length) {
            throw new NiceMbException("Value for field '%s' to long, max length is %d", field, length);
        } else if (value.length < length) {
            b = new byte[length];
            System.arraycopy(value, 0, b, 0, value.length);
        } else {
            b = value;
        }
        setValue(field, b);
    }
}
