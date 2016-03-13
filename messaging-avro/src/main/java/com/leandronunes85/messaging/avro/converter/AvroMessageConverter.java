package com.leandronunes85.messaging.avro.converter;

import com.leandronunes85.messaging.api.LogFormatEnforcer;
import com.leandronunes85.messaging.api.converter.Converter;
import com.leandronunes85.messaging.api.model.Headers;
import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.Serializer;
import com.leandronunes85.messaging.api.supplier.LazyDeserializerSupplier;
import com.leandronunes85.messaging.avro.model.AvroMessage;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AvroMessageConverter<T> implements Converter<Message<T>, AvroMessage> {

    private static final LogFormatEnforcer LOGGER = LogFormatEnforcer.loggerFor(AvroMessageConverter.class);

    private final Serializer<T> payloadSerializer;

    public AvroMessageConverter(Serializer<T> payloadSerializer) {
        this.payloadSerializer = requireNonNull(payloadSerializer);
    }

    @Override
    public AvroMessage convert(Message<T> toConvert) {
        LOGGER.trace(b -> b.operation("convert").and("toConvert", toConvert));

        AvroMessage.Builder builder = AvroMessage.newBuilder();

        builder.setHeaders(
                toConvert.getAllHeaders().stream().collect(toMap(Pair::getKey, Pair::getValue))
        );
        builder.setPayload(
                ByteBuffer.wrap(payloadSerializer.serialize(toConvert.getPayload()))
        );

        AvroMessage result = builder.build();

        LOGGER.debug(b -> b.operation("convert").and("toConvert", toConvert).and("result", result));

        return result;
    }

    @Override
    public Message<T> reverse(AvroMessage toConvert) {
        LOGGER.trace(b -> b.operation("reverse").and("toConvert", toConvert));

        Headers headers = new Headers(
                toConvert.getHeaders().entrySet().stream()
                        .map(e -> Pair.of(e.getKey().toString(), e.getValue().toString()))
                        .collect(toList())
        );
        Supplier<T> payload = new LazyDeserializerSupplier<>(payloadSerializer, toConvert.getPayload().array());
        Message<T> result = new Message<>(headers, payload);

        LOGGER.debug(b -> b.operation("reverse").and("toConvert", toConvert).and("result", result));

        return result;
    }
}
