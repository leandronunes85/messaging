package com.leandronunes85.messaging.api.supplier;

import com.leandronunes85.messaging.api.serializer.IntegerSerializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LazyDeserializerSupplierTest {

    private static final Integer EXPECTED = 10;

    private LazyDeserializerSupplier<Integer> victim;
    private IntegerSerializer integerSerializer;

    @BeforeMethod
    public void setUp() throws Exception {
        IntegerSerializer realSerializer = new IntegerSerializer();
        integerSerializer = spy(realSerializer);
        victim = LazyDeserializerSupplier.from(integerSerializer, realSerializer.serialize(EXPECTED));
    }

    @Test
    public void shouldNotDeserializeUntilRequested() throws Exception {
        verifyZeroInteractions(integerSerializer);
    }

    @Test
    public void shouldDeserializeWhenRequested() throws Exception {
        Integer actual = victim.get();
        verify(integerSerializer).deserialize(any(byte[].class));
        assertThat(actual).isEqualTo(EXPECTED);
    }

    @Test
    public void shouldOnlyDeserializeOnceWhenRequestedMultipleTimes() throws Exception {
        Integer actual1 = victim.get();
        Integer actual2 = victim.get();

        verify(integerSerializer, times(1)).deserialize(any(byte[].class));

        assertThat(actual1).isEqualTo(EXPECTED);
        assertThat(actual2).isEqualTo(EXPECTED);
        assertThat(actual1).isSameAs(actual2);
    }

    @Test
    public void shouldOnlyDeserializeOnceWhenRequestedByMultipleThreads() throws Exception {

        ExecutorService executorService = newFixedThreadPool(5);
        List<Future<Integer>> results = newArrayList();

        for (int i = 0; i < 10; i++) {
            results.add(
                    executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return victim.get();
                        }
                    })
            );
        }

        executorService.shutdown();

        verify(integerSerializer, times(1)).deserialize(any(byte[].class));
        for (Future<Integer> result : results) {
            assertThat(result.get()).isEqualTo(EXPECTED);
        }
    }
}