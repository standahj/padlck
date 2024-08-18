package com.cleverthis.interview.domain;

import java.util.Arrays;

/**
 * Implement the padlock cracking mechanism.
 * It can run as single thread or as multithreaded.
 */
public class PadlockCracker {

    private final IPadlock padlock;
    private final int start;
    private final int end;

    /**
     * Instantiate the Cracker algorithm instance for any padlock conforming to IPadlock signature.
     *
     * @param padlock the Padlock implementation accessor instance
     */
    public PadlockCracker(IPadlock padlock) {
        this(padlock,  0, padlock.getNumpadSize());
    }

    /**
     * Instantiate the algorithm instance, intended for eventual parallel execution (which is not feasible with current PadlockImpl).
     * @param padlock - the padlock accessor representing the 'physical' padlock
     * @param start - in single-threaded case, shall be 0, in multithreaded case - shall be the start index for the first digit
     * @param end - in single-threaded case, shall be 10, in multithreaded case - shall be the end index for the first digit
     */
    private PadlockCracker(IPadlock padlock, int start, int end) {
        this.padlock = padlock;
        this.start = start;
        this.end = end;
    }

    /**
     * Run the padlock crack algorithm.
     * This method complies with Callable&lt;int[]> signature for use with Executor service for potential multithreaded use.
     * While brute-force algorithm can be parallelized in general, due to the properties of the padlock (persistent internal buffer
     * and the longest running operation being the push of input digits to padlock's buffer), the multithreaded solution is NOT FEASIBLE in this task.
     *
     * @return - valid padlock combination or null if no such combination was found.
     */
    public int[] crackPadlock() {
        final int numpadSize = this.padlock.getNumpadSize();

        // Create an array with the digits applicable for our padlock
        // Assume we can choose a digit in range 0-9, unless the keypad size is larger than 10
        final int numDigits = Math.max(numpadSize, 10);
        final int[] applicableDigits = new int[numDigits+1];
        for (int i = 0; i <= numDigits ; i++) {
            applicableDigits[i] = i;
        }

        // Generate permutations of the numbers
        final boolean[] applied = new boolean[numDigits+1];
        final int[] currentPermutation = new int[numpadSize];

        return backtrack(applicableDigits, applied, currentPermutation, this.padlock, 0, this.start, this.end);
    }

    /**
     * Recursive num-pad incrementing, allows to explore each combination at given depth, and all permutations on lower depth.
     * @param applicableDigits array with digits to use
     * @param applied             flag to indicate that the digit and all permutations of following digits were already explored
     * @param currentPermutation  stores current permutation to be tested with the padlock
     * @param padlock             padlock accessor
     * @param depth               current depth (at which digit we iterate)
     * @param start               start range for the iteration (supplied from invoker for depth 0, otherwise 0)
     * @param end                 end range for the iteration  (supplied from invoker for depth 0, otherwise num of applicable digits)
     * @return the correct permutation or null if permutation was rejected by the padlock
     */
    private int[] backtrack(final int[] applicableDigits,
                            final boolean[] applied,
                            final int[] currentPermutation,
                            final IPadlock padlock,
                            final int depth,
                            final int start,
                            final int end) {
        boolean done = false;
        // Test if we are ready to unlock
        if (depth == currentPermutation.length) {
            // The currentPermutation is now a complete input candidate
            if (padlock.isPasscodeCorrect()) {
                System.out.println("YES, We cracked the code: " + Arrays.toString(currentPermutation));
                return currentPermutation;
            }
            return null;
        }

        for (int i = start; i < end && i < padlock.getNumpadSize() && !done; i++) {
            if (!applied[i]) {
                applied[i] = true;
                currentPermutation[depth] = applicableDigits[i];
                padlock.writeInputBuffer(applicableDigits[i], depth);
                done = backtrack(applicableDigits, applied, currentPermutation, padlock, depth + 1, 0, applicableDigits.length) != null;
                applied[i] = false; // Backtrack
            }
        }
        return done ? currentPermutation : null;
    }
}
