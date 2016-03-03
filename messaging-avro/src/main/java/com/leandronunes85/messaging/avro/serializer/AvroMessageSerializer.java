package com.leandronunes85.messaging.avro.serializer;

import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.Serializer;
import com.leandronunes85.messaging.avro.converter.AvroMessageConverter;
import com.leandronunes85.messaging.avro.model.AvroMessage;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class AvroMessageSerializer<T> implements Serializer<Message<T>> {

    private final AvroMessageConverter<T> messageConverter;
    private final DatumWriter<AvroMessage> datumWriter;
    private final DatumReader<AvroMessage> datumReader;

    public AvroMessageSerializer(Serializer<T> payloadSerializer) {
        this.messageConverter = new AvroMessageConverter<>(requireNonNull(payloadSerializer));
        this.datumWriter = new SpecificDatumWriter<>(AvroMessage.getClassSchema());
        this.datumReader = new SpecificDatumReader<>(AvroMessage.getClassSchema());
    }

    @Override
    public byte[] serialize(Message<T> obj) {
        AvroMessage avroMessage = messageConverter.convert(obj);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            datumWriter.write(avroMessage, encoder);
            encoder.flush();
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }

    @Override
    public Message<T> deserialize(byte[] bytes) {
        Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);

        try {
            AvroMessage avroMessage = datumReader.read(null, decoder);
            return messageConverter.reverse(avroMessage);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }
}
