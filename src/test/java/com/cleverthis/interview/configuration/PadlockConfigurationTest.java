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
        assertNotNull(cfg, "configuration should be created");
        assertEquals(PadlockAccessType.JAVA, cfg.accessType(), "Padlock access type should be JAVA");
        assertEquals(12, cfg.padlockKeypadSize(), "Keypad size set to 12");
        assertNull(cfg.instanceType(), "No instance type is specified in this test");
    }

    @Test
    void fromNumericValue() {
        // Given
        // note - will read main padlock-configuration.json
        PadlockConfiguration cfg = PadlockConfiguration.from("17");
        // Then
        assertNotNull(cfg, "configuration should be created");
        assertEquals(PadlockAccessType.JAVA, cfg.accessType(), "Padlock access type should be JAVA");
        assertEquals(17, cfg.padlockKeypadSize(), "Keypad size set to 17");
        assertNull(cfg.instanceType(), "No instance type is specified in this test");
    }

    @Test
    void fromExplicitConfig() {
        // Given
        PadlockConfiguration cfg = PadlockConfiguration.from("/test-padlock-configuration.json");
        // Then
        assertNotNull(cfg, "configuration should be created");
        assertEquals(cfg.accessType(), PadlockAccessType.JAVA, "Padlock access type should be CLI");
        assertEquals(11, cfg.padlockKeypadSize(), "Keypad size set to 11");
        assertNull(cfg.instanceType(), "No instance type is specified in this test");
    }

    @Test
    void fromExplicitCustomConfig() {
        // Given
        PadlockConfiguration cfg = PadlockConfiguration.from("/test-padlock-configuration-custom.json");
        // Then
        assertNotNull(cfg, "configuration should be created");
        assertEquals(cfg.accessType(), PadlockAccessType.CUSTOM,"Padlock access type should be CUSTOM");
        assertEquals(0, cfg.padlockKeypadSize(), "Keypad size is ignored");
        assertNotNull(cfg.instanceType(), "The custom instance type should be present");
        assertEquals("com.cleverthis.interview.domain.PadlockImplDelegate", cfg.instanceType().type(), "Only PadlockImplDelegate is supported");
    }
}