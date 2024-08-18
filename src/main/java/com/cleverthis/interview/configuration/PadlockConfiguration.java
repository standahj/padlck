package com.cleverthis.interview.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

public record PadlockConfiguration(
        PadlockAccessType accessType,
        int padlockKeypadSize,
        PadlockInstanceType instanceType
) {
    /**
     * Nested record to allow defining the constructor parameters of the custom class for the PadlockAccessor.
     * See padlock-configuration-custom.json resource file.
     *
     * @param type
     * @param value
     * @param literal
     */
    public record PadlockInstanceType (String type, PadlockInstanceType value, String literal) {
    }

    public static final String PADLOCK_DEFAULT_CONFIGURATION = "padlock-configuration.json";
    public static final int    PADLOCK_DEFAULT_KEYPAD_SIZE = 4;
    public static final PadlockAccessType PADLOCK_DEFAULT_ACCESS_TYPE = PadlockAccessType.JAVA;
    private static final Logger logger =  Logger.getLogger(PadlockConfiguration.class.getName());

    /**
     * Reads the padlock configuration from JSON format file.
     *
     * @param cfgArgument the configuration file or number representing the padlock keypad size.
     *                    If null, then the default config file, padlock-configuration.json, will be loaded.
     * @return the initialized PadlockConfiguration object.
     */
    public static PadlockConfiguration from(final String cfgArgument) {
        int keypadSize = PADLOCK_DEFAULT_KEYPAD_SIZE;
        try {
            final ObjectMapper configMapper = new ObjectMapper();

            if (cfgArgument == null) {
                //
                // read the default JSON config
                //
                return configMapper.readValue(
                        Thread.currentThread().getContextClassLoader().getResource(PADLOCK_DEFAULT_CONFIGURATION),
                        PadlockConfiguration.class);
            } else {
                //
                // try to read the first argument as custom configuration JSON file
                //
                try {
                    return configMapper.readValue(
                            Thread.currentThread().getContextClassLoader().getResource(cfgArgument),
                            PadlockConfiguration.class);
                } catch (Exception cfgException) {
                    //
                    // try if this is a numeric arg for keypad size
                    //
                    try {
                        keypadSize = Integer.parseInt(cfgArgument);
                    } catch (Exception intParseException) {
                        logger.log(Level.SEVERE, intParseException.getMessage(), intParseException);
                        logger.log(Level.INFO,"PadlockCracker expects single argument: padlock keypad size (int)");
                    }
                }
            }
        } catch (Exception configurationException) {
            logger.log(Level.SEVERE, configurationException.getMessage(), configurationException);
        }
        //
        // This returns default Padlock type (java) and keypad size from either the first argument
        // or the default value if the first arg was not numeric and not reference to JSON config file.
        //
        return new PadlockConfiguration(PADLOCK_DEFAULT_ACCESS_TYPE, keypadSize, null);
    }
}
