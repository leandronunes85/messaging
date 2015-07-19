package com.leandronunes85.messaging.avro.serializer;

import com.leandronunes85.messaging.api.model.Headers;
import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.IntegerSerializer;
import com.leandronunes85.messaging.api.serializer.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class AvroMessageSerializerTest {

    private AvroMessageSerializer<Integer> victim;

    private Serializer<Integer> payloadSerializer;

    @BeforeMethod
    public void setUp() throws Exception {
        payloadSerializer = spy(new IntegerSerializer());
        victim = new AvroMessageSerializer<>(payloadSerializer);
    }

    @Test
    public void shouldUseGivenSerializerToSerializePayload() throws Exception {
        Message<Integer> message = new Message<>(new Headers(), 10);

        victim.serialize(message);

        verify(payloadSerializer).serialize(10);
    }

    @Test
    public void shouldDeserializeSerializedData() throws Exception {
        Headers headers = new Headers();
        headers.put("Key1", "Value1");
        headers.put("Key2", "Value2");
        Message<Integer> expected = new Message<>(headers, 10);

        Message<Integer> actual = victim.deserialize(victim.serialize(expected));

        assertEquals(actual.getHeaders().get("Key1"), expected.getHeaders().get("Key1"));
        assertEquals(actual.getHeaders().get("Key2"), expected.getHeaders().get("Key2"));

        assertEquals(actual.getPayload().intValue(), 10);
    }

    @Test
    public void shouldLazilyDeserializePayload() throws Exception {
        Headers headers = new Headers();
        Message<Integer> expected = new Message<>(headers, 10);

        Message<Integer> actual = victim.deserialize(victim.serialize(expected));

        verify(payloadSerializer, never()).deserialize(any(byte[].class));

        actual.getPayload();

        verify(payloadSerializer).deserialize(any(byte[].class));
    }
}