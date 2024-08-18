package com.cleverthis.interview.domain;

import com.cleverthis.interview.configuration.PadlockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockBuilderTest {

    private static final PadlockConfiguration javaConfiguration = PadlockConfiguration.from("test-padlock-configuration.json");
    private static final PadlockConfiguration customConfiguration = PadlockConfiguration.from("test-padlock-configuration-custom.json");
    private static final PadlockConfiguration numericConfiguration = PadlockConfiguration.from("8");

    private PadlockBuilder builder;

    @BeforeEach
    void setUp() {
        this.builder = PadlockBuilder.newBuilder();
    }

    @Test
    void javaConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(javaConfiguration);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass());
        assertEquals(javaConfiguration.padlockKeypadSize(), padlock.getNumpadSize());
    }

    @Test
    void customConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(customConfiguration);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass());
        assertEquals(customConfiguration.instanceType().value().value().literal(), Integer.toString(padlock.getNumpadSize()));
    }

    @Test
    void numericConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(numericConfiguration);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass());
        assertEquals(8, padlock.getNumpadSize());
    }

    @Test
    void noConfigurationTest() {
        // given
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass());
        assertEquals(PadlockConfiguration.PADLOCK_DEFAULT_KEYPAD_SIZE, padlock.getNumpadSize());
    }

    @Test
    void keypadSizeConfigurationTest() {
        // given
        this.builder.withKeypadSize(21);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass());
        assertEquals(21, padlock.getNumpadSize());
    }
}