package com.cleverthis.interview.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockConfigurationTest {

    @Test
    void fromDefaultConfig() {
        // Given
        // note - will read main padlock-configuration.json
        PadlockConfiguration cfg = PadlockConfiguration.from(null);
        // Then
        assertEquals(PadlockAccessType.JAVA, cfg.accessType());
        assertEquals(12, cfg.padlockKeypadSize());
        assertNull(cfg.instanceType());
    }

    @Test
    void fromNumericValue() {
        // Given
        // note - will read main padlock-configuration.json
        PadlockConfiguration cfg = PadlockConfiguration.from("17");
        // Then
        assertEquals(PadlockAccessType.JAVA, cfg.accessType());
        assertEquals(17, cfg.padlockKeypadSize());
        assertNull(cfg.instanceType());
    }

    @Test
    void fromExplicitConfig() {
        // Given
        PadlockConfiguration cfg = PadlockConfiguration.from("test-padlock-configuration.json");
        // Then
        assertEquals(cfg.accessType(), PadlockAccessType.CLI);
        assertEquals(11, cfg.padlockKeypadSize());
        assertNull(cfg.instanceType());
    }

    @Test
    void fromExplicitCustomConfig() {
        // Given
        PadlockConfiguration cfg = PadlockConfiguration.from("test-padlock-configuration-custom.json");
        // Then
        assertEquals(cfg.accessType(), PadlockAccessType.CUSTOM);
        assertEquals(0, cfg.padlockKeypadSize());
        assertNotNull(cfg.instanceType());
        assertEquals("com.cleverthis.interview.domain.PadlockImplDelegate", cfg.instanceType().type());
    }
}