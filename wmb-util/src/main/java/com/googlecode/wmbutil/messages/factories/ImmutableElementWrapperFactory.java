package com.googlecode.wmbutil.messages.factories;

import com.google.common.base.Optional;
import com.googlecode.wmbutil.messages.MbElementWrapper;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbXPath;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public abstract class ImmutableElementWrapperFactory<T extends MbElementWrapper> {

  protected abstract T createWrapper(MbElement element);

  protected T evaluateXPath(MbMessage message, MbXPath xpath) throws MbException {
    return createWrapper(checkFirstElementExists(message, xpath));
  }

  /**
   * Get the header from the message object.
   *
   * @param message Message to get the header from
   * @return
   * @throws MbException
   */
  public T get(MbMessage message) throws MbException {
    return evaluateXPath(message, getMbXPath());
  }

  public Optional<T> tryGet(MbMessage msg) throws MbException {
    return exists(msg) ? Optional.of(get(msg)) : Optional.<T>absent();
  }

  private MbElement checkFirstElementExists(MbMessage message, MbXPath xpath) throws MbException {
    return checkExists(message, xpath).get(0);
  }

  private List<MbElement> checkExists(MbMessage message, MbXPath xpath) throws MbException {
    Optional<List<MbElement>> elements = tryCheckExists(message, xpath);
    if(elements.isPresent()) {
      return elements.get();
    }
    throw new NoSuchElementException(getPath() + " does not exist in message " + message);
  }

  private Optional<List<MbElement>> tryCheckExists(MbMessage message, MbXPath xpath) throws MbException {
    Object o = message.getRootElement().evaluateXPath(xpath);
    if (o instanceof List) {
      //noinspection unchecked
      List<MbElement> elements = (List<MbElement>) o;
      return elements.isEmpty() ? Optional.<List<MbElement>>absent() : Optional.of(elements);
    }
    return Optional.absent();
  }

  /**
   * Validate that header exists in the provided message.
   *
   * @param msg Message to get header from
   * @return True if the header exists, false otherwise
   * @throws MbException
   */
  public boolean exists(MbMessage msg) throws MbException {
    return tryCheckExists(msg, getMbXPath()).isPresent();
  }

  protected MbXPath getMbXPath() throws MbException {
    return new MbXPath(getPath());
  }

  protected abstract String getPath();

}
