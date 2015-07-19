package com.leandronunes85.messaging.api.serializer;

/**
 * Simple contract to be implemented by serialization/deserialization classes.
 * @param <T> The type this class (de)serializes.
 */
public interface Serializer<T> {

    byte[] serialize(T obj);

    T deserialize(byte[] bytes);

}
