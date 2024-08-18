package com.cleverthis.interview;

import com.cleverthis.interview.domain.PadlockCracker;
import com.cleverthis.interview.domain.PadlockImplDelegate;
import com.cleverthis.interview.padlock.PadlockImpl;

/**
 * This is a placeholder class showing a simple boilerplate.
 * This class is not required, so you can replace with your own architecture.
 */
public class Solution {

    /**
     * Solve the padlock setting from initialized Java padlock.
     * @param padlock  the Java padlock
     */
    public void solve(PadlockImpl padlock) {
        new PadlockCracker(new PadlockImplDelegate(padlock)).crackPadlock();
    }
}
