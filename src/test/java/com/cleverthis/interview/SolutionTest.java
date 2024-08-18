package com.cleverthis.interview;

import com.cleverthis.interview.padlock.PadlockImpl;

/**
 * This is a simple placeholder to show how unit test works.
 */
class SolutionTest extends SolutionTestBase {
    @Override
    protected void solve(PadlockImpl padlock) {
        new Solution().solve(padlock);
    }
}