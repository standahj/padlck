package com.cleverthis.interview;

import com.cleverthis.interview.configuration.PadlockConfiguration;
import com.cleverthis.interview.domain.IPadlock;
import com.cleverthis.interview.domain.PadlockBuilder;
import com.cleverthis.interview.domain.PadlockCracker;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point to the Padlock cracker tool that allows to configure the padlock from the external file or from the command line.
 */
public class PadlockCrackerApplication {

    private final Logger logger = Logger.getLogger(PadlockCrackerApplication.class.getName());

    /**
     * Tool main entry point.
     * @param argv CLI arguments
     */
    public static void main(String[] argv) {
        new PadlockCrackerApplication().solution(argv);
    }

    /**
     * Runs the padlock cracking algorithm using provided padlock configuration in the invocation argument.
     *
     * @param argv JVM command line arguments
     */
    private void solution(String... argv) {
        //
        // Read the Padlock configuration
        //
        final PadlockConfiguration padlockConfiguration = PadlockConfiguration.from(argv.length > 0 ? argv[0] : null);
        //
        // Instantiate padlock instance from configuration and also padlock cracker algorithm
        //
        final IPadlock padlock = PadlockBuilder.newBuilder().withPadlockConfiguration(padlockConfiguration).build();
        final PadlockCracker cracker = new PadlockCracker(padlock);
        //
        // Report the result
        //
        final int[] solution = cracker.execute();

        if (solution != null) {
            if (this.logger.isLoggable(Level.INFO)) {
                this.logger.info("Cracked solution: %s".formatted(Arrays.toString(solution)));
            }
        } else {
            this.logger.warning("No solution!");
        }
    }
}
