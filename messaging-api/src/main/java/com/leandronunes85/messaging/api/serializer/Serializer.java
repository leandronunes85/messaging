package com.leandronunes85.messaging.api.serializer;

public interface Serializer<T> {

    byte[] serialize(T obj);

    T deserialize(byte[] bytes);

}
