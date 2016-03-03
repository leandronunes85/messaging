package com.leandronunes85.messaging.api.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class HeadersTest {

    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";

    private Headers victim;

    @BeforeMethod
    public void setUp() throws Exception {
        victim = new Headers();
    }

    @Test
    public void shouldStartWithNoHeadersDefined() {
        assertThat(victim.getAll()).isEmpty();
    }

    @Test
    public void shouldReturnAnAbsentHeader() {

        Optional<String> actual = victim.get("NOT_EXISTENT_KEY");

        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnCorrectHeader() {
        victim.put(KEY, VALUE);
        Optional<String> actual = victim.get(KEY);

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(VALUE);
    }

    @Test
    public void shouldOverrideValueForSameKey() {

        victim.put(KEY, "SOME_OTHER_VALUE");
        victim.put(KEY, VALUE);
        Optional<String> actual = victim.get(KEY);

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(VALUE);
    }
}