package com.cleverthis.interview.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Top-level record for the essential Padlock configuration.
 * @param accessType define the Padlock access / implementation type. Defaults to JAVA.
 * @param padlockKeypadSize define the padlock keypad size. Defaults to 4,
 * @param instanceType Optional. Allows to specify a constructor for custom build of a Padlock if none of the predefined
 *                     implementation types does not instantiate the required padlock access type. Defaults to null.
 */
public record PadlockConfiguration(
        PadlockAccessType accessType,
        int padlockKeypadSize,
        PadlockInstanceType instanceType
) {
    /**
     * Nested record to allow defining the constructor parameters of the custom class for the Padlock builder.
     * See padlock-configuration-custom.json resource file.
     *
     * @param type
     * @param value
     * @param literal
     */
    public record PadlockInstanceType(String type, PadlockInstanceType value, String literal) {
    }

    public static final String PADLOCK_DEFAULT_CONFIGURATION = "/padlock-configuration.json";
    public static final int PADLOCK_DEFAULT_KEYPAD_SIZE = 4;
    public static final PadlockAccessType PADLOCK_DEFAULT_ACCESS_TYPE = PadlockAccessType.JAVA;
    private static final Logger logger = Logger.getLogger(PadlockConfiguration.class.getName());

    /**
     * Reads the padlock configuration from JSON format file. Uses the default config file if no command line argument is given.
     *
     * @param cfgArgument the configuration file or number representing the padlock keypad size.
     *                    If null, then the default config file, padlock-configuration.json, will be loaded.
     * @return the initialized PadlockConfiguration object.
     */
    public static PadlockConfiguration from(final String cfgArgument) {
        try {
            if (cfgArgument == null) {
                //
                // read the default JSON config
                //
                final ObjectMapper configMapper = new ObjectMapper();
                URL resource = PadlockConfiguration.class.getResource(PADLOCK_DEFAULT_CONFIGURATION);
                return configMapper.readValue(resource, PadlockConfiguration.class);
            }
        } catch (IOException configurationException) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, configurationException.getMessage(), configurationException);
            }
        }
        return fromArgv(cfgArgument);
    }

    /**
     * Reads the configuration from the non-null command line argument. The argument can either be a JSON file reference
     * or number representing the keypad size.
     * @param cfgArgument non-null command line argument. String value representing the File name or number of keys.
     * @return configuration built from the input specification, if the input is JSON config file name.
     *          If the input is a number, than it returns configuration with access type JAVA and specified keypad size.
     *          If the input is neither of the above, then return configuration with defaults: access type JAVA and keypad size 4.
     */
    public static PadlockConfiguration fromArgv(String cfgArgument) {
        //
        // try to read the first argument as custom configuration JSON file
        //
        int keypadSize = -1;
        try {
            URL resource = PadlockConfiguration.class.getResource(cfgArgument);
            if (resource != null) {
                final ObjectMapper configMapper = new ObjectMapper();
                return configMapper.readValue(resource, PadlockConfiguration.class);
            }
            keypadSize = Integer.parseInt(cfgArgument);
        } catch (IOException | NumberFormatException cfgException) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("PadlockCracker expects single argument: padlock keypad size (int), there was an exception while parsing the arg: %s"
                        .formatted(cfgException.getMessage()));
            }
        }
        //
        // try if this is a numeric arg for keypad size
        //
        if (keypadSize == -1) {
            try {
                keypadSize = Integer.parseInt(cfgArgument);
            } catch (NumberFormatException intParseException) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("PadlockCracker expects single argument: padlock keypad size (int), there was an exception whil eparsing the arg: %s"
                            .formatted(intParseException.getMessage()));
                }
                keypadSize = PADLOCK_DEFAULT_KEYPAD_SIZE;
            }
        }
        //
        // This returns default Padlock type (java) and keypad size from either the first argument
        // or the default value if the first arg was not numeric and not reference to JSON config file.
        //
        return new PadlockConfiguration(PADLOCK_DEFAULT_ACCESS_TYPE, keypadSize, null);
    }
}
