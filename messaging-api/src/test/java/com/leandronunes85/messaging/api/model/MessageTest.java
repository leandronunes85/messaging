package com.leandronunes85.messaging.api.model;

import com.google.common.base.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    private static final Collection<Pair<String, String>> HEADERS = newArrayList(of("Key1", "Value1"));
    private static final String PAYLOAD = "Payload";

    private Message<String> victim;

    @BeforeMethod
    public void setUp() throws Exception {
        victim = new Message<>(HEADERS, PAYLOAD);
    }

    @Test
    public void shouldReturnCorrectHeaders() throws Exception {
        Collection<Pair<String, String>> actual = victim.getAllHeaders();
        assertThat(actual).containsExactlyElementsOf(HEADERS);
    }

    @Test
    public void shouldAddHeader() throws Exception {
        String expected = "Value2";

        victim.putHeader("Key2", expected);
        Optional<String> actual = victim.getHeader("Key2");

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    public void shouldRemoveHeader() throws Exception {

        Optional<String> actual = victim.removeHeader("Key1");

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo("Value1");

        actual = victim.getHeader("Key1");
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnCorrectPayload() throws Exception {
        String actual = victim.getPayload();
        assertThat(actual).isEqualTo(PAYLOAD);
    }
}