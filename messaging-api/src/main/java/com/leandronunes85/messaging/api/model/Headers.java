package com.leandronunes85.messaging.api.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Headers to be used by the {@link Message} class. It stores key-value pairs (both represented by {@link String}s for
 * simplicity).
 */
public class Headers {
    private static final Logger LOG = LoggerFactory.getLogger(Headers.class);
    private static final Function<Map.Entry<String, String>, Pair<String, String>> ENTRY_TO_PAIR =
            new Function<Map.Entry<String, String>, Pair<String, String>>() {
                public Pair<String, String> apply(Map.Entry<String, String> entry) {
                    return Pair.of(entry.getKey(), entry.getValue());
                }
            };

    private final HashMap<String, String> headersMap;

    public Headers() {
        this.headersMap = newHashMap();
    }

    public Headers(Collection<Pair<String, String>> keyValuePairs) {
        this();
        for (Pair<String, String> keyValuePair : keyValuePairs) {
            this.headersMap.put(keyValuePair.getKey(), keyValuePair.getValue());
        }
    }

    public void put(String key, String value) {
        LOG.trace("op=put, key='{}', value='{}'", key, value);
        String oldValue = headersMap.put(checkNotNull(key), checkNotNull(value));
        LOG.debug("op=put, key='{}', value='{}', oldValue='{}'", key, value, oldValue);
    }

    public Optional<String> get(String key) {
        LOG.trace("op=get, key='{}'", key);
        Optional<String> result = fromNullable(headersMap.get(key));
        LOG.debug("op=get, key='{}', result='{}'", key, result);
        return result;
    }

    public Collection<Pair<String, String>> getAll() {
        LOG.trace("op=getAll");
        Collection<Pair<String, String>> result = from(headersMap.entrySet()).transform(ENTRY_TO_PAIR).toSet();
        LOG.debug("op=getAll, result='{}'", result);
        return result;
    }

    public Optional<String> remove(String key) {
        LOG.trace("op=remove, key='{}'", key);
        Optional<String> result = fromNullable(headersMap.remove(key));
        LOG.debug("op=remove, key='{}', result='{}'", key, result);
        return result;
    }
}
