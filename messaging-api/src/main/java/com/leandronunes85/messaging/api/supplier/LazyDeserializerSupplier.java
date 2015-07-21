package com.leandronunes85.messaging.api.supplier;

import com.google.common.base.Supplier;
import com.leandronunes85.messaging.api.serializer.Deserializer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link Supplier} implementation that lazily deserializes a given byte[].
 * @param <T> Type of the object supplied.
 */
public class LazyDeserializerSupplier<T> implements Supplier<T> {

    public static <T> LazyDeserializerSupplier<T> from(Deserializer<T> serializer, byte[] bytes) {
        return new LazyDeserializerSupplier<T>(serializer, bytes);
    }

    private final Deserializer<T> serializer;
    private final byte[] bytes;
    private T obj = null;

    private LazyDeserializerSupplier(Deserializer<T> serializer, byte[] bytes) {
        this.serializer = checkNotNull(serializer);
        this.bytes = checkNotNull(bytes);
    }

    public synchronized T get() {
        if (obj == null) {
            obj = serializer.deserialize(bytes);
        }
        return obj;
    }
}
