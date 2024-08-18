package com.cleverthis.interview.domain;

import com.cleverthis.interview.padlock.PadlockImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockCrackerTest {

    @Test
    void crackPadlock() {
        // given
        final int keypadSize = 9;
        final IPadlock padlock = new PadlockImplDelegate(new PadlockImpl(keypadSize));
        final PadlockCracker cracker = new PadlockCracker(padlock);
        // when
        final int[] solution= cracker.crackPadlock();
        // then
        assertNotNull(solution);
        assertEquals(keypadSize, solution.length);
        assertTrue(padlock.isPasscodeCorrect());
    }
}