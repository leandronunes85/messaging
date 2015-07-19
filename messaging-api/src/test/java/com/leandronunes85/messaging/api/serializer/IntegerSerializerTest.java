package com.leandronunes85.messaging.api.serializer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.assertEquals;

public class IntegerSerializerTest {

    private static final Random RANDOM = new Random();

    private IntegerSerializer victim;

    @BeforeMethod
    public void setUp() throws Exception {
        victim = new IntegerSerializer();
    }

    @Test(invocationCount = 20)
    public void shouldSerializeAndDeserializeCorrectly() throws Exception {
        Integer expected = RANDOM.nextInt();

        Integer actual = victim.deserialize(victim.serialize(expected));

        assertEquals(actual, expected);
    }
}