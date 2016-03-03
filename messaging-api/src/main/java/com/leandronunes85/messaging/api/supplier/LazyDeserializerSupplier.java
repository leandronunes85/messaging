package com.leandronunes85.messaging.api.supplier;

import com.leandronunes85.messaging.api.LogFormatEnforcer;
import com.leandronunes85.messaging.api.serializer.Deserializer;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.TRACE;

/**
 * {@link Supplier} implementation that lazily deserializes a given byte[].
 *
 * @param <T> Type of the object supplied.
 */
public class LazyDeserializerSupplier<T> implements Supplier<T> {

    private static final LogFormatEnforcer LOGGER = LogFormatEnforcer.loggerFor(LazyDeserializerSupplier.class);

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
        LOGGER.log(TRACE, b -> b.operation("deserializeBytes")
                                .message("Obj will be deserialized")
                                .and("serializer", serializer)
                                .and("bytesLength", bytes.length));

        this.obj = serializer.deserialize(bytes);

        LOGGER.log(DEBUG, b -> b.operation("deserializeBytes")
                                .message("Obj was deserialized")
                                .and("serializer", serializer)
                                .and("bytesLength", bytes.length)
                                .and("obj", obj));
    }
}
