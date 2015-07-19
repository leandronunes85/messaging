package com.leandronunes85.messaging.api.model;

import com.google.common.base.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
        assertTrue(victim.getAll().isEmpty());
    }

    @Test
    public void shouldReturnAnAbsentHeader() {

        Optional<String> actual = victim.get("NOT_EXISTENT_KEY");

        assertFalse(actual.isPresent());
    }

    @Test
    public void shouldReturnCorrectHeader() {
        victim.put(KEY, VALUE);
        Optional<String> actual = victim.get(KEY);

        assertTrue(actual.isPresent());
        assertEquals(actual.get(), VALUE);
    }

    @Test
    public void shouldOverrideValueForSameKey() {

        victim.put(KEY, "SOME_OTHER_VALUE");
        victim.put(KEY, VALUE);
        Optional<String> actual = victim.get(KEY);

        assertTrue(actual.isPresent());
        assertEquals(actual.get(), VALUE);
    }
}