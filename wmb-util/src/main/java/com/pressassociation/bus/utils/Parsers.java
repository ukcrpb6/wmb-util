package com.pressassociation.bus.utils;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Bob Browning <bob.browning@pressassociation.com>
 */
public class Parsers {

    public enum BODY_PARSERS {
        MRM, XMLNSC, XMLNS, XML, SOAP, DataObject, JMSMap, JMSStream, MIME, BLOB, IDOC, JSON, DFDL
    }

    private static Supplier<Set<BODY_PARSERS>> bodyParserSet = Suppliers.memoize(new Supplier<Set<BODY_PARSERS>>() {
        @Override public Set<BODY_PARSERS> get() {
            return Sets.immutableEnumSet(Arrays.asList(BODY_PARSERS.values()));
        }
    });

    private static Supplier<Set<String>> bodyParserNameSet = Suppliers.memoize(new Supplier<Set<String>>() {
        @Override public Set<String> get() {
            return ImmutableSet.copyOf(Iterables.transform(getBodyParserSet(), new Function<BODY_PARSERS, String>() {
                @Override public String apply(BODY_PARSERS input) {
                    return input.name();
                }
            }));
        }
    });

    public static Set<BODY_PARSERS> getBodyParserSet() {
        return bodyParserSet.get();
    }

    public static Set<String> getBodyParserNameSet() {
        return bodyParserNameSet.get();
    }

}
