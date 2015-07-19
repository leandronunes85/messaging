package com.leandronunes85.messaging.api.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MessageTest {

    private static final Headers HEADERS = new Headers();
    private static final String PAYLOAD = "Payload";

    private Message<String> victim;

    @BeforeMethod
    public void setUp() throws Exception {
        victim = new Message<String>(HEADERS, PAYLOAD);
    }

    @Test
    public void shouldReturnCorrectHeaders() throws Exception {
        Headers actual = victim.getHeaders();
        assertEquals(actual, HEADERS);
    }

    @Test
    public void shouldReturnCorrectPayload() throws Exception {
        String actual = victim.getPayload();
        assertEquals(actual, PAYLOAD);
    }
}