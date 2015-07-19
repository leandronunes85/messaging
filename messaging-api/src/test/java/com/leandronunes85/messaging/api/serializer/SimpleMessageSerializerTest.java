package com.leandronunes85.messaging.api.serializer;

import com.leandronunes85.messaging.api.model.Headers;
import com.leandronunes85.messaging.api.model.Message;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SimpleMessageSerializerTest {

    private SimpleMessageSerializer<Integer> victim;

    private Serializer<Headers> headersSerializer;

    private Serializer<Integer> payloadSerializer;

    @BeforeMethod
    public void setUp() throws Exception {
        headersSerializer = mock(Serializer.class);
        payloadSerializer = spy(new IntegerSerializer());

        when(headersSerializer.serialize(any(Headers.class)))
                .thenReturn(new byte[]{0, 1, 2, 3});

        when(headersSerializer.deserialize(any(byte[].class)))
                .thenReturn(mock(Headers.class));

        victim = new SimpleMessageSerializer<>(headersSerializer, payloadSerializer);
    }

    @Test
    public void shouldUseGivenSerializersToSerialize() {
        Message<Integer> message = new Message<>(mock(Headers.class), 10);

        victim.serialize(message);

        verify(headersSerializer).serialize(any(Headers.class));
        verify(payloadSerializer).serialize(10);
    }

    @Test
    public void shouldUseGivenSerializersToDeserializeLazily() {
        Message<Integer> message = new Message<>(mock(Headers.class), 10);

        byte[] bytes = victim.serialize(message);
        Message<Integer> actual = victim.deserialize(bytes);

        verify(headersSerializer, never()).deserialize(any(byte[].class));
        verify(payloadSerializer, never()).deserialize(any(byte[].class));

        actual.getHeaders();
        actual.getPayload();

        verify(headersSerializer).deserialize(any(byte[].class));
        verify(payloadSerializer).deserialize(any(byte[].class));
    }

}