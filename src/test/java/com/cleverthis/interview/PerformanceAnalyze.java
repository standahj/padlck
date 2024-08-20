package com.cleverthis.interview;

import com.cleverthis.interview.padlock.PadlockImpl;

/**
 * Performance test but not mean to run in unit test.
 */
public class PerformanceAnalyze {

    private static final int TOTAL_RUN = 500;
    private static final int NUMPAD_SIZE = 9;

    static {
        System.out.println("Total run: " + TOTAL_RUN);
        System.out.println("Numpad size: " + NUMPAD_SIZE);
    }

    public static void main(String[] args) {
        new PerformanceAnalyze().analyze(args);
    }

    private void solve(PadlockImpl padlock) {
        new Solution().solve(padlock);
    }

    private void analyze(String... args) {
        long timeSum = 0;
        long writeSum = 0;
        for (int i = 0; i < TOTAL_RUN; i++) {
            PadlockImpl padlock = new PadlockImpl(NUMPAD_SIZE);
            padlock.resetCounter();
            long start = System.currentTimeMillis();
            solve(padlock);
            long end = System.currentTimeMillis();
            if (!padlock.isPasscodeCorrect()) throw new IllegalStateException(
                    "Invalid solution: passcode not correct after return");
            long dT = end - start;
            timeSum += dT;
            writeSum += padlock.getWriteCounter();
            System.out.println("Run #" + (i + 1) + ": time: " + dT + "ms; write: " + padlock.getWriteCounter());
        }
        System.out.println("Run time sum: " + timeSum + "ms");
        System.out.println("Write sum: " + writeSum);
        double avgTime = timeSum / (double) TOTAL_RUN;
        double avgWrite = writeSum / (double) TOTAL_RUN;
        System.out.println("Avg run time: " + avgTime + "ms");
        System.out.println("Avg write: " + avgWrite);
        System.out.println("Calculated estimate avg run time: " + (avgTime / 1000 + avgWrite) + "s");
    }
}
