package com.leandronunes85.messaging.api.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Generic message representation. These are the instances that will be serialized and sent through the wire. This class
 * knows nothing about the serialization/deserialization process though.
 *
 * @param <T> The type of the payload this message stores
 */
public class Message<T> {

    private final Headers headers;
    private final Supplier<T> payload;

    public Message(T payload) {
        this(new Headers(), () -> requireNonNull(payload));
    }

    public Message(Supplier<T> payload) {
        this(new Headers(), requireNonNull(payload));
    }

    public Message(Collection<Pair<String, String>> headers, T payload) {
        this(new Headers(requireNonNull(headers)), () -> requireNonNull(payload));
    }

    private Message(Headers headers, Supplier<T> payload) {
        this.headers = requireNonNull(headers);
        this.payload = requireNonNull(payload);
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    public Optional<String> getHeader(String key) {
        return headers.get(key);
    }

    public Collection<Pair<String, String>> getAllHeaders() {
        return headers.getAll();
    }

    public Optional<String> removeHeader(String key) {
        return headers.remove(key);
    }

    public T getPayload() {
        return payload.get();
    }
}
