package com.googlecode.wmbutil;

import com.pressassociation.utils.ThrowingSupplier;
import com.pressassociation.utils.ThrowingSuppliers;
import org.junit.Assert;
import org.junit.Test;

public class ThrowingSupplierTest {

    @Test
    public void testThrowingSupplier() throws Exception {
        Assert.assertNull(ThrowingSuppliers.ofInstance(null).get());
        Assert.assertTrue(ThrowingSuppliers.ofInstance(true).get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowingSupplierThrows() throws Exception {
        ThrowingSupplier<Void, Exception> throwingSupplier = new ThrowingSupplier<Void, Exception>() {
            @Override public Void get() throws Exception {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertNotNull(throwingSupplier);
        throwingSupplier.get();
    }

    @Test
    public void testThrowingSupplierMemoize() throws Exception {
        ThrowingSupplier<Object, Exception> supplier = ThrowingSuppliers.memoize(new ThrowingSupplier<Object, Exception>() {
            @Override public Object get() throws Exception {
                return new Object();
            }
        });
        Assert.assertNotNull(supplier);
        Object o = supplier.get();
        Assert.assertSame(o, supplier.get());
    }
}