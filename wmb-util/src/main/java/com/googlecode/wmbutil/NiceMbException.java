/*
 * Copyright 2007 (C) Callista Enterprise.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.googlecode.wmbutil;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.ibm.broker.plugin.MbBrokerException;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbUserException;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class NiceMbException extends MbUserException {

    private static final long serialVersionUID = -5760540385903728797L;

    private String message;

    public NiceMbException(Object source, String msg) {
        super(source.getClass().getName(), "", "", "", msg, new Object[0]);
        this.message = msg;
    }

    public NiceMbException(String msg) {
        super("", "", "", "", msg, new Object[0]);
        this.message = msg;
    }

    public NiceMbException(String msg, Object... parameters) {
        super("", "", "", "", String.format(msg, parameters), new Object[0]);
        this.message = msg;
    }

    private NiceMbException(StackTraceElement element, String msg) {
        super(element.getClassName(), element.getMethodName(), "", "", msg, new Object[0]);
        this.message = msg;
    }

    /**
     * Propagate MbException and MbBrokerException instances.
     *
     * @param t - Throwable to be thrown if possible
     * @throws MbException
     */
    public static void propagateIfPossible(@Nullable Throwable t) throws MbException {
        Throwables.propagateIfPossible(t, MbBrokerException.class);
        Throwables.propagateIfPossible(t, MbException.class);
    }

    /**
     * Propagate an exception, wrapping it as an NiceMbException.
     *
     * @param t - Throwable to be propagated as an NiceMbException
     * @return NiceMbException wrapping the provided throwable
     */
    public static MbException propagate(Throwable t) throws MbException {
        propagateIfPossible(checkNotNull(t));
        //noinspection ThrowableResultOfMethodCallIgnored
        if (t.getStackTrace() != null && t.getStackTrace().length > 0) {
            StackTraceElement[] st = t.getStackTrace();
            final NiceMbException ex = new NiceMbException(st[0], t.getMessage());
            ex.setStackTrace(st);
            return ex;
        }

        return new NiceMbException(t.getMessage());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("class", getClassName())
                .add("method", getMethodName())
                .add("source", getMessageSource())
                .add("key", getMessageKey())
                .add("message", message).toString();
    }
}