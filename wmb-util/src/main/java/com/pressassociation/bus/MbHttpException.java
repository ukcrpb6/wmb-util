package com.pressassociation.bus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ibm.broker.plugin.MbUserException;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class MbHttpException extends MbUserException {

    private static final String MESSAGE_NUMBER = "2950";
    private static final String MESSAGE_CATALOG_NAME = "BIPmsgs";

    private MbHttpException(String className, String methodName, String lineNumber, String messageCatalog, String additionalText, Object[] inserts) {
        super(className, methodName, lineNumber, messageCatalog, additionalText, inserts);
    }

    public static MbHttpException buildException(int statusCode, String message, Throwable cause, Object... arguments) throws MbUserException {
        final MbHttpException e = createRawException(statusCode, message, arguments);
        e.initCause(cause);
        return e;
    }

    public static MbHttpException buildException(int statusCode, String message, Object... arguments) throws MbUserException {
        final MbHttpException e = createRawException(statusCode, message, arguments);
        e.fillInStackTrace();
        return e;
    }

    private static MbHttpException createRawException(int statusCode, String message, Object... arguments) {
        final StackTraceElement caller = Thread.currentThread().getStackTrace()[1];
        List<Object> inserts = Lists.newArrayList();
        inserts.add(statusCode);
        inserts.add(Preconditions.checkNotNull(message));
        inserts.addAll(Arrays.asList(arguments));
        return new MbHttpException(
                caller.getClassName(), caller.getMethodName(), MESSAGE_CATALOG_NAME, MESSAGE_NUMBER, "", inserts.toArray());
    }
}