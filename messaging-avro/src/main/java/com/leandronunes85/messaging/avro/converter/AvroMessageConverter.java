package com.leandronunes85.messaging.avro.converter;

import com.google.common.collect.Maps;
import com.leandronunes85.messaging.api.converter.Converter;
import com.leandronunes85.messaging.api.model.Message;
import com.leandronunes85.messaging.api.serializer.Serializer;
import com.leandronunes85.messaging.avro.model.AvroMessage;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.leandronunes85.messaging.api.supplier.LazyDeserializerSupplier.from;

public class AvroMessageConverter<T> implements Converter<Message<T>, AvroMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(AvroMessageConverter.class);

    private final Serializer<T> payloadSerializer;

    public AvroMessageConverter(Serializer<T> payloadSerializer) {
        this.payloadSerializer = checkNotNull(payloadSerializer);
    }

    @Override
    public AvroMessage convert(Message<T> toConvert) {
        LOG.trace("op=convert, toConvert='{}'", toConvert);

        AvroMessage.Builder builder = AvroMessage.newBuilder();

        Map<CharSequence, CharSequence> headerMap = Maps.newHashMap();
        for (Pair<String, String> header : toConvert.getAllHeaders()) {
            headerMap.put(header.getKey(), header.getValue());
        }
        byte[] payloadBytes = payloadSerializer.serialize(toConvert.getPayload());

        builder.setHeaders(headerMap);
        builder.setPayload(ByteBuffer.wrap(payloadBytes));

        AvroMessage result = builder.build();

        LOG.debug("op=convert, toConvert='{}', result='{}'", toConvert, result);

        return result;
    }

    @Override
    public Message<T> reverse(AvroMessage toConvert) {

        LOG.trace("op=reverse, toConvert='{}'", toConvert);

        Message<T> result = new Message<>(from(payloadSerializer, toConvert.getPayload().array()));

        for (Map.Entry<CharSequence, CharSequence> header : toConvert.getHeaders().entrySet()) {
            result.putHeader(header.getKey().toString(), header.getValue().toString());
        }

        LOG.debug("op=reverse, toConvert='{}', result='{}'", toConvert, result);

        return result;
    }
}
