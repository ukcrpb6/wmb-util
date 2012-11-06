package com.pressassociation.utils;

/**
* @author Bob Browning <bob.browning@pressassociation.com>
*/
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
