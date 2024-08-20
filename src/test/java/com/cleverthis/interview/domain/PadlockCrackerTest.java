package com.cleverthis.interview.domain;

import com.cleverthis.interview.padlock.PadlockImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockCrackerTest {

    @Test
    void execute() {
        // given
        final int keypadSize = 9;
        final IPadlock padlock = new PadlockImplDelegate(new PadlockImpl(keypadSize));
        final PadlockCracker cracker = new PadlockCracker(padlock);
        // when
        final int[] solution= cracker.execute();
        // then
        assertNotNull(solution, "Solution should exists");
        assertEquals(keypadSize, solution.length, "Solution length and actual keypad size should match");
        assertTrue(padlock.isPasscodeCorrect(), "Padlock should acknowledge the success");
    }
}