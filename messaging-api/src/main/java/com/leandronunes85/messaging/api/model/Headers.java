package com.leandronunes85.messaging.api.model;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

/**
 * Headers to be used by the {@link Message} class. It stores key-value pairs (both represented by {@link String}s for
 * simplicity).
 */
public class Headers {
    private static final Logger LOG = LoggerFactory.getLogger(Headers.class);

    private final HashMap<String, String> headersMap;

    public Headers() {
        this.headersMap = new HashMap<>();
    }

    public Headers(Collection<Pair<String, String>> keyValuePairs) {
        this();
        for (Pair<String, String> keyValuePair : keyValuePairs) {
            this.headersMap.put(keyValuePair.getKey(), keyValuePair.getValue());
        }
    }

    public void put(String key, String value) {
        LOG.trace("op=put, key='{}', value='{}'", key, value);
        String oldValue = headersMap.put(requireNonNull(key), requireNonNull(value));
        LOG.debug("op=put, key='{}', value='{}', oldValue='{}'", key, value, oldValue);
    }

    public Optional<String> get(String key) {
        LOG.trace("op=get, key='{}'", key);
        Optional<String> result = ofNullable(headersMap.get(key));
        LOG.debug("op=get, key='{}', result='{}'", key, result);
        return result;
    }

    public Collection<Pair<String, String>> getAll() {
        LOG.trace("op=getAll");
        Collection<Pair<String, String>> result = headersMap.entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue()))
                .collect(toSet());
        LOG.debug("op=getAll, result='{}'", result);
        return result;
    }

    public Optional<String> remove(String key) {
        LOG.trace("op=remove, key='{}'", key);
        Optional<String> result = ofNullable(headersMap.remove(key));
        LOG.debug("op=remove, key='{}', result='{}'", key, result);
        return result;
    }
}
