package com.cleverthis.interview.padlock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockTest {

    @Test
    void testInstantiationRejectNonPositiveNumpadSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new PadlockImpl(0));
        assertThrows(IllegalArgumentException.class,
                () -> new PadlockImpl(-500));
    }

    @Test
    void testInstantiationAcceptPositiveNumpadSize() {
        assertDoesNotThrow(() -> new PadlockImpl(1));
        assertDoesNotThrow(() -> new PadlockImpl(500));
    }

    @Test
    void testInstantiationRest() {
        PadlockImpl padlock = new PadlockImpl(5);
        for (int i = 0; i < 5; i++) {
            // ensure input buffer is uninitialized
            // should return null when first set
            assertNull(padlock.writeInputBuffer(i, i));
        }
        // should return false
        assertFalse(padlock.isPasscodeCorrect());
    }

    @Test
    void testRejectInvalidInput() {
        PadlockImpl padlock = new PadlockImpl(5);
        for (int i = 0; i < 3; i++) {
            padlock.writeInputBuffer(i, i);
        }
        // partially initialized, should fail
        assertThrows(IllegalStateException.class, padlock::isPasscodeCorrect);
        for (int i = 0; i < 2; i++) {
            padlock.writeInputBuffer(i + 3, i);
        }
        // now we have duplicated value, should fail too
        assertThrows(IllegalStateException.class, padlock::isPasscodeCorrect);
        for (int i = 0; i < 2; i++) {
            padlock.writeInputBuffer(i + 3, i + 3);
        }
        // now we fixed everything, we should have no error
        assertDoesNotThrow(padlock::isPasscodeCorrect);
    }

    @Test
    void testRejectInvalidInputBufferAddressAndValue() {
        PadlockImpl padlock = new PadlockImpl(5);
        // test address
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> padlock.writeInputBuffer(-1, 1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> padlock.writeInputBuffer(-10, 1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> padlock.writeInputBuffer(5, 1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> padlock.writeInputBuffer(50, 1));
        // test value
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(1, -1));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(1, -10));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(1, 5));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(1, 50));
        // test both
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(-10, -1));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(5, -10));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(50, 5));
        assertThrows(IllegalArgumentException.class, () -> padlock.writeInputBuffer(-1, 50));
    }

}