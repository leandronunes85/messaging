package com.leandronunes85.messaging.avro.serializer;

import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.IntegerSerializer;
import com.leandronunes85.messaging.api.serializer.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        Message<Integer> message = new Message<>(10);

        victim.serialize(message);

        verify(payloadSerializer).serialize(10);
    }

    @Test
    public void shouldDeserializeSerializedData() throws Exception {
        Message<Integer> expected = new Message<>(10);
        expected.putHeader("Key1", "Value1");
        expected.putHeader("Key2", "Value2");

        Message<Integer> actual = victim.deserialize(victim.serialize(expected));

        assertThat(actual.getHeader("Key1").get()).isEqualTo("Value1");
        assertThat(actual.getHeader("Key2").get()).isEqualTo("Value2");
        assertThat(actual.getPayload()).isEqualTo(10);
    }

    @Test
    public void shouldLazilyDeserializePayload() throws Exception {
        Message<Integer> expected = new Message<>(10);

        Message<Integer> actual = victim.deserialize(victim.serialize(expected));

        verify(payloadSerializer, never()).deserialize(any(byte[].class));

        actual.getPayload();

        verify(payloadSerializer).deserialize(any(byte[].class));
    }
}