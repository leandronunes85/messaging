package com.leandronunes85.messaging.api.serializer;

import com.leandronunes85.messaging.api.model.Headers;
import com.leandronunes85.messaging.api.model.Message;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.leandronunes85.messaging.api.supplier.LazyDeserializerSupplier.from;
import static com.leandronunes85.messaging.api.serializer.IntegerSerializer.INT_BYTE_COUNT;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.subarray;

public class SimpleMessageSerializer<T> implements Serializer<Message<T>> {

    private final Serializer<Headers> headersSerializer;
    private final Serializer<T> payloadSerializer;
    private final IntegerSerializer intSerializer;

    public SimpleMessageSerializer(Serializer<Headers> headersSerializer,
                                   Serializer<T> payloadSerializer) {
        this.headersSerializer = checkNotNull(headersSerializer);
        this.payloadSerializer = checkNotNull(payloadSerializer);
        this.intSerializer = new IntegerSerializer();
    }

    public byte[] serialize(Message<T> obj) {
        byte[] headersBytes = headersSerializer.serialize(obj.getHeaders());
        byte[] payloadBytes = payloadSerializer.serialize(obj.getPayload());
        byte[] headersSizeBytes = intSerializer.serialize(headersBytes.length);
        return addAll(addAll(headersSizeBytes, headersBytes), payloadBytes);
    }

    public Message<T> deserialize(byte[] bytes) {
        byte[] headersSizeBytes = subarray(bytes, 0, INT_BYTE_COUNT);
        int headersSize = intSerializer.deserialize(headersSizeBytes);
        byte[] headersBytes = subarray(bytes, INT_BYTE_COUNT, headersSize + INT_BYTE_COUNT);
        byte[] payloadBytes = subarray(bytes, headersSize + INT_BYTE_COUNT, bytes.length);

        return new Message<T>(from(headersSerializer, headersBytes), from(payloadSerializer, payloadBytes));
    }
}
