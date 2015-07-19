package com.leandronunes85.messaging.api.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.FluentIterable.from;

public class Headers {

    private static final Function<Map.Entry<String, String>, Pair<String, String>> ENTRY_TO_PAIR =
            new Function<Map.Entry<String, String>, Pair<String, String>>() {
                public Pair<String, String> apply(Map.Entry<String, String> entry) {
                    return Pair.of(entry.getKey(), entry.getValue());
                }
            };

    private final Map<String, String> headersMap = new HashMap<String, String>();

    public void put(String key, String value) {
        headersMap.put(checkNotNull(key), checkNotNull(value));
    }

    public Optional<String> get(String key) {
        return fromNullable(headersMap.get(key));
    }

    public Collection<Pair<String, String>> getAll() {
        return from(headersMap.entrySet()).transform(ENTRY_TO_PAIR).toSet();
    }
}
