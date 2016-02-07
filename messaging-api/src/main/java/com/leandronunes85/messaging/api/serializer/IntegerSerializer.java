package com.leandronunes85.messaging.api.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Simple {@link Serializer} implementation for {@link Integer}s
 */
public class IntegerSerializer implements Serializer<Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(IntegerSerializer.class);
    private static final int INT_BYTE_COUNT = 4;

    public byte[] serialize(Integer obj) {
        LOG.trace("op=serialize, obj={}", obj);
        byte[] result = ByteBuffer.allocate(INT_BYTE_COUNT).putInt(obj).array();
        LOG.debug("op=serialize, obj={}, result={}", obj, result);
        return result;
    }

    public Integer deserialize(byte[] bytes) {
        LOG.trace("op=deserialize, bytesLength={}", bytes.length);
        Integer result = ByteBuffer.wrap(bytes).getInt();
        LOG.debug("op=deserialize, bytesLength={}, result={}", bytes.length, result);
        return result;
    }
}
