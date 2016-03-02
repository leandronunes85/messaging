package com.leandronunes85.messaging.api.supplier;

import com.leandronunes85.messaging.api.serializer.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * {@link Supplier} implementation that lazily deserializes a given byte[].
 * @param <T> Type of the object supplied.
 */
public class LazyDeserializerSupplier<T> implements Supplier<T> {

    private static final Logger LOG = LoggerFactory.getLogger(LazyDeserializerSupplier.class);

    public static <T> LazyDeserializerSupplier<T> from(Deserializer<T> serializer, byte[] bytes) {
        return new LazyDeserializerSupplier<>(serializer, bytes);
    }

    private final Deserializer<T> serializer;
    private final byte[] bytes;
    private T obj = null;

    private LazyDeserializerSupplier(Deserializer<T> serializer, byte[] bytes) {
        this.serializer = requireNonNull(serializer);
        this.bytes = requireNonNull(bytes);
    }

    public synchronized T get() {
        if (this.obj == null) {
            deserializeBytes();
        }
        return this.obj;
    }

    private void deserializeBytes() {
        LOG.trace("op=deserializeBytes, serializer='{}', bytesLength={}, msg='Obj will be deserialized.'",
                serializer, bytes.length);

        this.obj = serializer.deserialize(bytes);

        LOG.debug("op=deserializeBytes, serializer='{}', bytesLength={}, obj='{}', msg='Obj was deserialized.'",
                serializer, bytes.length, obj);
    }
}
