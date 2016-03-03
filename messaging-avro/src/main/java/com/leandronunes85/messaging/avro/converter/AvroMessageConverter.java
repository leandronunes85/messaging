package com.leandronunes85.messaging.avro.converter;

import com.leandronunes85.messaging.api.LogFormatEnforcer;
import com.leandronunes85.messaging.api.converter.Converter;
import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.Serializer;
import com.leandronunes85.messaging.avro.model.AvroMessage;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;

import static com.leandronunes85.messaging.api.supplier.LazyDeserializerSupplier.from;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.TRACE;

public class AvroMessageConverter<T> implements Converter<Message<T>, AvroMessage> {

    private static final LogFormatEnforcer LOGGER = LogFormatEnforcer.loggerFor(AvroMessageConverter.class);

    private final Serializer<T> payloadSerializer;

    public AvroMessageConverter(Serializer<T> payloadSerializer) {
        this.payloadSerializer = requireNonNull(payloadSerializer);
    }

    @Override
    public AvroMessage convert(Message<T> toConvert) {
        LOGGER.log(TRACE, b -> b.operation("convert").and("toConvert", toConvert));

        AvroMessage.Builder builder = AvroMessage.newBuilder();

        builder.setHeaders(
                toConvert.getAllHeaders().stream().collect(toMap(Pair::getKey, Pair::getValue))
        );
        builder.setPayload(
                ByteBuffer.wrap(payloadSerializer.serialize(toConvert.getPayload()))
        );

        AvroMessage result = builder.build();

        LOGGER.log(DEBUG, b -> b.operation("convert").and("toConvert", toConvert).and("result", result));

        return result;
    }

    @Override
    public Message<T> reverse(AvroMessage toConvert) {
        LOGGER.log(TRACE, b -> b.operation("reverse").and("toConvert", toConvert));

        Message<T> result = new Message<>(from(payloadSerializer, toConvert.getPayload().array()));
        toConvert.getHeaders().entrySet().forEach(e -> result.putHeader(e.getKey().toString(), e.getValue().toString()));

        LOGGER.log(DEBUG, b -> b.operation("reverse").and("toConvert", toConvert).and("result", result));

        return result;
    }
}
