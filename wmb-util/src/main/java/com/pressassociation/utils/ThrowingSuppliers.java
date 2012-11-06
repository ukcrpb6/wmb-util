package com.pressassociation.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
* @author Bob Browning <bob.browning@pressassociation.com>
*/
public class ThrowingSuppliers {
    /**
       * Returns a supplier which caches the instance retrieved during the first
       * call to {@code get()} and returns that value on subsequent calls to
       * {@code get()}. See:
       * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
       *
       * <p>The returned supplier is thread-safe. The supplier's serialized form
       * does not contain the cached value, which will be recalculated when {@code
       * get()} is called on the reserialized instance.
       *
       * <p>If {@code delegate} is an instance created by an earlier call to {@code
       * memoize}, it is returned directly.
       */
      public static <T, E extends Exception> ThrowingSupplier<T, E> memoize(ThrowingSupplier<T, E> delegate) {
        return (delegate instanceof MemoizingSupplier)
            ? delegate
            : new MemoizingSupplier<T,E>(Preconditions.checkNotNull(delegate));
      }

      @VisibleForTesting
      static class MemoizingSupplier<T,E extends Exception> implements ThrowingSupplier<T,E>, Serializable {
        final ThrowingSupplier<T,E> delegate;
        transient volatile boolean initialized;
        // "value" does not need to be volatile; visibility piggy-backs
        // on volatile read of "initialized".
        transient T value;

        MemoizingSupplier(ThrowingSupplier<T, E> delegate) {
          this.delegate = delegate;
        }

        @Override
        public T get() throws E {
          // A 2-field variant of Double Checked Locking.
          if (!initialized) {
            synchronized (this) {
              if (!initialized) {
                T t = delegate.get();
                value = t;
                initialized = true;
                return t;
              }
            }
          }
          return value;
        }

        private static final long serialVersionUID = 0;
      }
}
