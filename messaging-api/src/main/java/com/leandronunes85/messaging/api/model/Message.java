package com.leandronunes85.messaging.api.model;

import com.google.common.base.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.ofInstance;

/**
 * Generic message representation. These are the instances that will be serialized and sent through the wire. This class
 * knows nothing about the serialization/deserialization process though.
 * @param <T> The type of the payload this message stores
 */
public class Message<T> {

    private final Supplier<Headers> headers;
    private final Supplier<T> payload;

    public Message(Headers headers, T payload) {
        this(ofInstance(checkNotNull(headers)), ofInstance(checkNotNull(payload)));
    }

    public Message(Headers headers, Supplier<T> payload) {
        this(ofInstance(checkNotNull(headers)), checkNotNull(payload));
    }

    public Message(Supplier<Headers> headers, Supplier<T> payload) {
        this.headers = checkNotNull(headers);
        this.payload = checkNotNull(payload);
    }

    public Headers getHeaders() {
        return headers.get();
    }

    public T getPayload() {
        return payload.get();
    }
}
