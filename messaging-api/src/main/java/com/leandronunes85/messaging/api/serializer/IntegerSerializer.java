package com.leandronunes85.messaging.api.serializer;

import java.nio.ByteBuffer;

public class IntegerSerializer implements Serializer<Integer> {

    public static final int INT_BYTE_COUNT = 4;

    public byte[] serialize(Integer obj) {
        return ByteBuffer.allocate(INT_BYTE_COUNT).putInt(obj).array();
    }

    public Integer deserialize(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}
