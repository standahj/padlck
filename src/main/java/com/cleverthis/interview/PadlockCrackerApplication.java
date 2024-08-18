package com.cleverthis.interview;
import com.cleverthis.interview.configuration.PadlockConfiguration;
import com.cleverthis.interview.domain.IPadlock;
import com.cleverthis.interview.domain.PadlockBuilder;
import com.cleverthis.interview.domain.PadlockCracker;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PadlockCrackerApplication {

    private static final Logger logger =  Logger.getLogger(PadlockCrackerApplication.class.getName());

    public static void main(String[] argv) {
        //
        // Read the Padlock configuration
        //
        final PadlockConfiguration padlockConfiguration = PadlockConfiguration.from(argv.length > 0 ? argv[0] : null);
        //
        // Instantiate padlock instance from configuration and also padlock cracker algorithm
        //
        final IPadlock padlock = PadlockBuilder.newBuilder().withPadlockConfiguration(padlockConfiguration).build();
        final PadlockCracker  cracker = new PadlockCracker(padlock);
        //
        // Report the result
        //
        final int[] solution = cracker.crackPadlock();

        if (solution != null) {
            logger.log(Level.INFO, "Cracked solution: " + Arrays.toString(solution));
        } else {
            logger.warning("No solution!");
        }
    }
}
