package com.pressassociation.bus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ibm.broker.plugin.MbUserException;

import java.util.Arrays;
import java.util.List;

public class MbHttpException extends MbUserException {

    private static final String MESSAGE_CATALOG_BIP2950 = "2950";

    private MbHttpException(String className, String methodName, String lineNumber, String messageCatalog, String additionalText, Object[] inserts) {
        super(className, methodName, lineNumber, messageCatalog, additionalText, inserts);
    }

    public static MbHttpException buildException(int statusCode, String message, Object... arguments) throws MbUserException {
        final StackTraceElement caller = Thread.currentThread().getStackTrace()[1];
        List<Object> inserts = Lists.newArrayList();
        inserts.add(statusCode);
        inserts.add(Preconditions.checkNotNull(message));
        inserts.addAll(Arrays.asList(arguments));
        MbHttpException e = new MbHttpException(
                caller.getClassName(), caller.getMethodName(), "", MESSAGE_CATALOG_BIP2950, "", inserts.toArray());
        e.fillInStackTrace();
        return e;
    }
}