package com.leandronunes85.messaging.avro.converter;

import com.leandronunes85.messaging.api.model.Headers;
import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.IntegerSerializer;
import com.leandronunes85.messaging.avro.model.AvroMessage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class AvroMessageConverterTest {

    private AvroMessageConverter<Integer> victim;

    private IntegerSerializer payloadSerializer;

    @BeforeMethod
    public void setUp() throws Exception {
        payloadSerializer = spy(new IntegerSerializer());
        victim = new AvroMessageConverter<>(payloadSerializer);
    }

    @Test
    public void shouldConvertHeaders() throws Exception {
        Headers headers = new Headers();
        headers.put("Key1", "Value1");
        headers.put("Key2", "Value2");
        Message<Integer> message = new Message(headers, 10);

        AvroMessage actual = victim.convert(message);

        assertEquals(actual.getHeaders().get("Key1"), "Value1");
        assertEquals(actual.getHeaders().get("Key2"), "Value2");
    }

    @Test
    public void shouldUseSerializerToSerializePayload() throws Exception {
        Message<Integer> message = new Message(new Headers(), 10);

        victim.convert(message);

        verify(payloadSerializer).serialize(10);
    }
}