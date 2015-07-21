package com.leandronunes85.messaging.avro.serializer;

import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.IntegerSerializer;
import com.leandronunes85.messaging.api.serializer.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
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

        assertEquals(actual.getHeader("Key1"), expected.getHeader("Key1"));
        assertEquals(actual.getHeader("Key2"), expected.getHeader("Key2"));

        assertEquals(actual.getPayload().intValue(), 10);
    }

    @Test
    public void shouldLazilyDeserializePayload() throws Exception {
        Message<Integer> expected = new Message<>(10);

        Message<Integer> actual = victim.deserialize(victim.serialize(expected));

        verify(payloadSerializer, never()).deserialize(any(byte[].class));

        actual.getPayload();

        verify(payloadSerializer).deserialize(any(byte[].class));
    }

    public void produce() {
        // Prepare serializers:
        // You code this one
        Serializer<MyDomainObject> myDomainObjectSerializer = new MyDomainObjectSerializer();
        // And the library provides you with this one
        Serializer<Message<MyDomainObject>> serializer = new AvroMessageSerializer<>(myDomainObjectSerializer);

        // Create your domain object
        MyDomainObject myDomainObject = new MyDomainObject();
        // Use it to create a Message
        Message<MyDomainObject> message = new Message<>(myDomainObject);
        // And set all the headers you want
        message.putHeader("year", "2015");

        // Now transform the whole message into a byte array
        byte[] bytes = serializer.serialize(message);
    }

    public void consume() {
        // Prepare serializers:
        // You code this one
        Serializer<MyDomainObject> myDomainObjectSerializer = new MyDomainObjectSerializer();
        // And the library provides you with this one
        Serializer<Message<MyDomainObject>> serializer = new AvroMessageSerializer<>(myDomainObjectSerializer);


        byte[] bytes = null; //...;
        // Deserialize the message. Only headers will be deserialized here
        Message<MyDomainObject> message = serializer.deserialize(bytes);

        // Check the headers
        if ("2015".equals(message.getHeader("year").get())) {
            // This is where the payload deserialization occurs, when you're sure you'll need it!
            processThisYearMessage(message.getPayload());
        }
    }

    class MyDomainObjectSerializer implements Serializer<MyDomainObject> {

        @Override
        public byte[] serialize(MyDomainObject obj) {
            return new byte[0];
        }

        @Override
        public MyDomainObject deserialize(byte[] bytes) {
            return null;
        }
    }

    class MyDomainObject {

    }
}