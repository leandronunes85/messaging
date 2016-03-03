package com.leandronunes85.messaging.api.serializer;

import com.leandronunes85.messaging.api.LogFormatEnforcer;

import java.nio.ByteBuffer;

import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.TRACE;

/**
 * Simple {@link Serializer} implementation for {@link Integer}s
 */
public class IntegerSerializer implements Serializer<Integer> {

    private static final LogFormatEnforcer LOGGER = LogFormatEnforcer.loggerFor(IntegerSerializer.class);
    private static final int INT_BYTE_COUNT = 4;

    public byte[] serialize(Integer obj) {
        LOGGER.log(TRACE, b -> b.operation("serialize").and("obj", obj));
        byte[] result = ByteBuffer.allocate(INT_BYTE_COUNT).putInt(obj).array();
        LOGGER.log(DEBUG, b -> b.operation("serialize").and("obj", obj).and("result", result));
        return result;
    }

    public Integer deserialize(byte[] bytes) {
        LOGGER.log(TRACE, b -> b.operation("deserialize").and("bytesLength", bytes.length));
        Integer result = ByteBuffer.wrap(bytes).getInt();
        LOGGER.log(DEBUG, b -> b.operation("deserialize").and("bytesLength", bytes.length).and("result", result));
        return result;
    }
}
