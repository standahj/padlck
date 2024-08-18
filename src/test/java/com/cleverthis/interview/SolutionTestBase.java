package com.cleverthis.interview;

import com.cleverthis.interview.padlock.PadlockImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a base class for verifying the correctness of the solution.
 */
public abstract class SolutionTestBase {
    
    /**
     * Implement your solution in this function.
     * */
    protected abstract void solve(PadlockImpl padlock);

    protected void verify(int numpadSize) {
        PadlockImpl padlock = new PadlockImpl(numpadSize);
        solve(padlock);
        assertTrue(padlock.isPasscodeCorrect());
    }

    @Test
    void verify1to7() {
        for (int i = 1; i <= 7; i++) {
            verify(i);
        }
    }
}
