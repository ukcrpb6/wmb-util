package com.pressassociation.utils;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public final class ThrowingSuppliers {

    private ThrowingSuppliers() {}

    /**
     * Returns a new supplier which is the composition of the provided function
     * and supplier. In other words, the new supplier's value will be computed by
     * retrieving the value from {@code supplier}, and then applying
     * {@code function} to that value. Note that the resulting supplier will not
     * call {@code supplier} or invoke {@code function} until it is called.
     */
    public static <F, T, E extends Exception> ThrowingSupplier<T, E> compose(
            Function<? super F, T> function, ThrowingSupplier<F, E> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(supplier);
        return new SupplierComposition<F, T, E>(function, supplier);
    }

    private static class SupplierComposition<F, T, E extends Exception>
            implements ThrowingSupplier<T, E>, Serializable {
        final Function<? super F, T> function;
        final ThrowingSupplier<F, E> supplier;

        SupplierComposition(Function<? super F, T> function, ThrowingSupplier<F, E> supplier) {
            this.function = function;
            this.supplier = supplier;
        }

        @Override
        public T get() throws E {
            return function.apply(supplier.get());
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a supplier which caches the instance retrieved during the first
     * call to {@code get()} and returns that value on subsequent calls to
     * {@code get()}. See:
     * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
     * <p/>
     * <p>The returned supplier is thread-safe. The supplier's serialized form
     * does not contain the cached value, which will be recalculated when {@code
     * get()} is called on the reserialized instance.
     * <p/>
     * <p>If {@code delegate} is an instance created by an earlier call to {@code
     * memoize}, it is returned directly.
     */
    public static <T, E extends Exception> ThrowingSupplier<T, E> memoize(ThrowingSupplier<T, E> delegate) {
        return (delegate instanceof MemoizingThrowingSupplier)
                ? delegate
                : new MemoizingThrowingSupplier<T, E>(Preconditions.checkNotNull(delegate));
    }

    @VisibleForTesting
    static class MemoizingThrowingSupplier<T, E extends Exception> implements ThrowingSupplier<T, E>, Serializable {
        final ThrowingSupplier<T, E> delegate;
        transient volatile boolean initialized;
        // "value" does not need to be volatile; visibility piggy-backs
        // on volatile read of "initialized".
        transient T value;

        MemoizingThrowingSupplier(ThrowingSupplier<T, E> delegate) {
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

    /**
     * Returns a supplier that caches the instance supplied by the delegate and
     * removes the cached value after the specified time has passed. Subsequent
     * calls to {@code get()} return the cached value if the expiration time has
     * not passed. After the expiration time, a new value is retrieved, cached,
     * and returned. See:
     * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
     * <p/>
     * <p>The returned supplier is thread-safe. The supplier's serialized form
     * does not contain the cached value, which will be recalculated when {@code
     * get()} is called on the reserialized instance.
     *
     * @param duration the length of time after a value is created that it
     *                 should stop being returned by subsequent {@code get()} calls
     * @param unit     the unit that {@code duration} is expressed in
     * @throws IllegalArgumentException if {@code duration} is not positive
     * @since 2.0
     */
    public static <T, E extends Exception> ThrowingSupplier<T, E> memoizeWithExpiration(
            ThrowingSupplier<T, E> delegate, long duration, TimeUnit unit) {
        return new ExpiringMemoizingThrowingSupplier<T, E>(delegate, duration, unit);
    }

    @VisibleForTesting
    static class ExpiringMemoizingThrowingSupplier<T, E extends Exception>
            implements ThrowingSupplier<T, E>, Serializable {
        final ThrowingSupplier<T, E> delegate;
        final long durationNanos;
        transient volatile T value;
        // The special value 0 means "not yet initialized".
        transient volatile long expirationNanos;

        ExpiringMemoizingThrowingSupplier(
                ThrowingSupplier<T, E> delegate, long duration, TimeUnit unit) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument(duration > 0);
        }

        @Override
        public T get() throws E {
            // Another variant of Double Checked Locking.
            //
            // We use two volatile reads.  We could reduce this to one by
            // putting our fields into a holder class, but (at least on x86)
            // the extra memory consumption and indirection are more
            // expensive than the extra volatile reads.
            long nanos = expirationNanos;
            long now = System.nanoTime();
            if (nanos == 0 || now - nanos >= 0) {
                synchronized (this) {
                    if (nanos == expirationNanos) {  // recheck for lost race
                        T t = delegate.get();
                        value = t;
                        nanos = now + durationNanos;
                        // In the very unlikely event that nanos is 0, set it to 1;
                        // no one will notice 1 ns of tardiness.
                        expirationNanos = (nanos == 0) ? 1 : nanos;
                        return t;
                    }
                }
            }
            return value;
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a supplier that always supplies {@code instance}.
     */
    public static <T, E extends Exception> ThrowingSupplier<T, E> ofInstance(@Nullable T instance) {
        return new SupplierOfInstance<T, E>(instance);
    }

    private static class SupplierOfInstance<T, E extends Exception>
            implements ThrowingSupplier<T, E>, Serializable {
        final T instance;

        SupplierOfInstance(@Nullable T instance) {
            this.instance = instance;
        }

        @Override
        public T get() throws E {
            return instance;
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a supplier whose {@code get()} method synchronizes on
     * {@code delegate} before calling it, making it thread-safe.
     */
    public static <T, E extends Exception> ThrowingSupplier<T, E> synchronizedSupplier(ThrowingSupplier<T, E> delegate) {
        return new ThreadSafeThrowingSupplier<T, E>(Preconditions.checkNotNull(delegate));
    }

    private static class ThreadSafeThrowingSupplier<T, E extends Exception>
            implements ThrowingSupplier<T, E>, Serializable {
        final ThrowingSupplier<T, E> delegate;

        ThreadSafeThrowingSupplier(ThrowingSupplier<T, E> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T get() throws E {
            synchronized (delegate) {
                return delegate.get();
            }
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a function that accepts a supplier and returns the result of
     * invoking {@link Supplier#get} on that supplier.
     *
     * @since 8.0
     */
    @Beta
    @SuppressWarnings("unchecked") // SupplierFunction works for any T.
    public static <T, E extends Exception> Function<ThrowingSupplier<T, E>, T> supplierFunction() {
        return (Function) SupplierFunction.INSTANCE;
    }

    private enum SupplierFunction implements Function<Supplier<?>, Object> {
        INSTANCE;

        @Override
        public Object apply(Supplier<?> input) {
            return input.get();
        }
    }
}
