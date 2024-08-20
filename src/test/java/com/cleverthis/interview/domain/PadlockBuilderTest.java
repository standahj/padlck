package com.cleverthis.interview.domain;

import com.cleverthis.interview.configuration.PadlockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockBuilderTest {

    private static final PadlockConfiguration javaConfiguration = PadlockConfiguration.from("/test-padlock-configuration.json");
    private static final PadlockConfiguration unsupportedConfiguration = PadlockConfiguration.from("/test-unsupported-padlock-configuration.json");
    private static final PadlockConfiguration customConfiguration = PadlockConfiguration.from("/test-padlock-configuration-custom.json");
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
        assertEquals(PadlockImplDelegate.class, padlock.getClass(), "Should be of correct class");
        assertEquals(javaConfiguration.padlockKeypadSize(), padlock.getNumpadSize(), "Configured and actual keypad size should match");
    }

    @Test
    void unsupportedConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(unsupportedConfiguration);
        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            this.builder.build();
        });
        // then
        assertEquals("This Padlock type is not supported in v1.0: CLI", thrown.getMessage());
    }

    @Test
    void customConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(customConfiguration);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass(), "Should be of correct class");
        assertEquals(customConfiguration.instanceType().value().value().literal(), Integer.toString(padlock.getNumpadSize()), "Configured and actual keypad size should match");
    }

    @Test
    void numericConfigurationTest() {
        // given
        this.builder.withPadlockConfiguration(numericConfiguration);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass(), "Should be of correct class");
        assertEquals(8, padlock.getNumpadSize(), "Configured and actual keypad size should match");
    }

    @Test
    void noConfigurationTest() {
        // given
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass(), "Should be of correct class");
        assertEquals(PadlockConfiguration.PADLOCK_DEFAULT_KEYPAD_SIZE, padlock.getNumpadSize(), "Configured and actual keypad size should match");
    }

    @Test
    void keypadSizeConfigurationTest() {
        // given
        this.builder.withKeypadSize(21);
        // when
        final IPadlock padlock = this.builder.build();
        // then
        assertEquals(PadlockImplDelegate.class, padlock.getClass(), "Should be of correct class");
        assertEquals(21, padlock.getNumpadSize(), "Configured and actual keypad size should match");
    }
}