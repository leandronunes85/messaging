package com.leandronunes85.messaging.api.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
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

    private static final Function<Map.Entry<String, String>, Pair<String, String>> ENTRY_TO_PAIR =
            new Function<Map.Entry<String, String>, Pair<String, String>>() {
                public Pair<String, String> apply(Map.Entry<String, String> entry) {
                    return Pair.of(entry.getKey(), entry.getValue());
                }
            };

    private final Map<String, String> headersMap;

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
        headersMap.put(checkNotNull(key), checkNotNull(value));
    }

    public Optional<String> get(String key) {
        return fromNullable(headersMap.get(key));
    }

    public Collection<Pair<String, String>> getAll() {
        return from(headersMap.entrySet()).transform(ENTRY_TO_PAIR).toSet();
    }

    public Optional<String> remove(String key) {
        return fromNullable(headersMap.remove(key));
    }
}
