package com.googlecode.wmbutil.messages;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.googlecode.wmbutil.NiceMbException;
import com.googlecode.wmbutil.messages.factories.DefaultMbElementWrapperFactory;
import com.googlecode.wmbutil.messages.factories.MbElementWrapperFactory;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class MbElementWrapper {

    private static final MbElementWrapperFactory DEFAULT_ELEMENT_WRAPPER = new DefaultMbElementWrapperFactory();

    private MbElement wrappedElm;

    public MbElementWrapper(MbElement elm) {
        this.wrappedElm = elm;
    }

    public MbElement getMbElement() {
        return wrappedElm;
    }

    public boolean isReadOnly() {
        return getMbElement().isReadOnly();
    }

    public Optional<MbElementWrapper> getField(String field) throws MbException {
        return getField(field, DEFAULT_ELEMENT_WRAPPER, DefaultMbElementWrapper.class);
    }

    public Optional<MbElementWrapper> getField(String field,
                                               final MbElementWrapperFactory factory,
                                               final Class<? extends MbElementWrapper> adapterType) throws MbException {
      return Optional.fromNullable(getMbElement().getFirstElementByPath(field)).transform(
          new Function<MbElement, MbElementWrapper>() {
            @Override public MbElementWrapper apply(MbElement input) {
              return factory.getAdapter(input, adapterType);
            }
          });
    }


    public MbElementWrapper getOrCreateField(String field) throws MbException {
      return getOrCreateField(field, DEFAULT_ELEMENT_WRAPPER, DefaultMbElementWrapper.class);
    }

    public MbElementWrapper getOrCreateField(String field,
                                             MbElementWrapperFactory factory,
                                             Class<? extends MbElementWrapper> adapterType) throws MbException {
      Optional<MbElementWrapper> adapter = getField(field, factory, adapterType);
      if(adapter.isPresent()) {
        return adapter.get();
      }
      return factory.getAdapter(
          getMbElement().createElementAsLastChild(MbElement.TYPE_UNKNOWN, field, null), adapterType);
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
