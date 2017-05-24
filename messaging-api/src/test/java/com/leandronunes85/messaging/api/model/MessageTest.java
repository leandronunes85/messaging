package com.leandronunes85.messaging.api.model;

import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    private static final String PAYLOAD = "Payload";

    private Message<String> victim;

    @DataProvider
    public static Object[][] invalidHeadersAndPayload() {
        return new Object[][]{
                {null, PAYLOAD},
                {new Headers(), null},
                {null, null}
        };
    }

    @DataProvider
    public static Object[][] invalidHeadersAndSupplier() {
        Supplier<String> goodSupplier = () -> PAYLOAD;
        return new Object[][] {
                {null, goodSupplier},
                {new Headers(), null},
                {null, null}
        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        victim = new Message<>(new Headers(singletonList(Pair.of("Key1", "Value1"))), PAYLOAD);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldCheckIfPayloadIsNotNullAtConstructionTime() throws Exception {
        new Message<>(null);
    }

    @Test(expectedExceptions = NullPointerException.class, dataProvider = "invalidHeadersAndPayload")
    public void shouldCheckIfBothHeadersAndPayloadAreNotNullAtConstructionTime(Headers headers, String payload) throws Exception {
        new Message<>(headers, payload);
    }

    @Test(expectedExceptions = NullPointerException.class, dataProvider = "invalidHeadersAndSupplier")
    public void shouldCheckIfBothHeadersAndSupplierAreNotNullAtConstructionTime(Headers headers, Supplier<String> payloadSupplier) throws Exception {
        new Message<>(headers, payloadSupplier);
    }

    @Test
    public void shouldReturnCorrectHeaders() throws Exception {
        Collection<Pair<String, String>> actual = victim.getAllHeaders();
        assertThat(actual).containsExactlyElementsOf(new Headers(singletonList(Pair.of("Key1", "Value1"))).getAll());
    }

    @Test
    public void shouldAddHeader() throws Exception {
        String expected = "Value2";

        victim.putHeader("Key2", expected);
        Optional<String> actual = victim.getHeader("Key2");

        assertThat(actual).contains(expected);
    }

    @Test
    public void shouldRemoveHeader() throws Exception {

        victim.removeHeader("Key1");

        Optional<String> actual = victim.getHeader("Key1");

        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldReturnRemovedHeader() throws Exception {
        Optional<String> actual = victim.removeHeader("Key1");

        assertThat(actual).contains("Value1");
    }

    @Test
    public void shouldReturnCorrectPayload() throws Exception {
        String actual = victim.getPayload();

        assertThat(actual).isEqualTo(PAYLOAD);
    }
}