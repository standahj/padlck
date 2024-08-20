package com.cleverthis.interview.domain;

import com.cleverthis.interview.configuration.PadlockAccessType;
import com.cleverthis.interview.configuration.PadlockConfiguration;
import com.cleverthis.interview.padlock.PadlockImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Builder (factory) pattern, builds the padlock instance from the provided padlock configuration.
 */
public class PadlockBuilder {

    private PadlockConfiguration padlockConfiguration = null;
    private Integer keypadSize = null;

    public static PadlockBuilder newBuilder() {
        return new PadlockBuilder();
    }

    /**
     * A padlock configuration to be used to build the Padlock.
     * @param padlockConfiguration the configuration to be used to build the Padlock instance.
     * @return this builder for chained invocation.
     */
    public PadlockBuilder withPadlockConfiguration(PadlockConfiguration padlockConfiguration) {
        this.padlockConfiguration = padlockConfiguration;
        return this;
    }

    /**
     * A keypad size to be used to build the JAVA access type padlock if no other configuration is provided.
     * If both configuration object and the keypad size are provided, then the value of the explicitly provided keypad size
     * overrides the value in the configuration object.
     * @param keypadSize numeric keypad size.
     * @return this builder for chained invocation.
     */
    public PadlockBuilder withKeypadSize(int keypadSize) {
        this.keypadSize = Integer.valueOf(keypadSize);
        return this;
    }

    /**
     * Builder - build the IPadlock instance based on the provided configuration or keypadSize.
     * If the configuration is missing, it will use Java padlock type and default keypad size, unless desired keypadSize was provided.
     *
     * @return the IPadlock instance.
     */
    public IPadlock build() {
        // establish the requested values with precedence.
        final PadlockAccessType _accessType =
                this.padlockConfiguration != null ? this.padlockConfiguration.accessType() : PadlockConfiguration.PADLOCK_DEFAULT_ACCESS_TYPE;
        int _keypadSize = PadlockConfiguration.PADLOCK_DEFAULT_KEYPAD_SIZE;
        if (this.padlockConfiguration != null) {
           _keypadSize = this.padlockConfiguration.padlockKeypadSize();
        }
        if (this.keypadSize != null) {
            _keypadSize = this.keypadSize; // explicitly given keypad size overrides the configuration file value
        }
        // build the requested padlock type
        switch (_accessType) {
            case CLI: {
                throw new IllegalArgumentException("This Padlock type is not supported in v1.0: " + _accessType);
            }
            case CUSTOM: {
                IPadlock accessor = this.buildCustom();
                if (accessor != null) {
                    return accessor;
                }
                // if custom fails to build, fall-back through to the default
            }
            case JAVA:
            default:
                return new PadlockImplDelegate(new PadlockImpl(_keypadSize));
        }
    }

    /**
     * Build the Padlock instance for CUSTOM configuration.
     *
     * @return IPadlock instance or null if not possible to build one.
     */
    private IPadlock buildCustom() {
        if (this.padlockConfiguration.instanceType() != null) {
            return (IPadlock) instantiateInstanceType(this.padlockConfiguration.instanceType());
        }
        // if something went sideways....
        return null;
    }

    /**
     * Build the Padlock accessor class from the chain of constructor declarations
     * This uses simplified logic to showcase how ths can be done
     * There are no edge case checks here, as this methods should throw exception if it is not possible to build the Padlock instance.
     * The method is called recursively if the constructor argument is a class instance.
     *
     * @param padlockInstanceType configuration - chain of constructors
     * @return The instantiated class for the given invocation. Top invocation shall be the instance of IPadlock implementation class.
     * @throws RuntimeException if it is not possible to build the accessor from the configuration.
     */
    private Object instantiateInstanceType(PadlockConfiguration.PadlockInstanceType padlockInstanceType) {
        try {
            final Class<?> instancClass = Class.forName(padlockInstanceType.type());
            // this is for possible future cases, current implementation has no no-args constructors for existing candidates
            if (padlockInstanceType.value() == null) {
                final Constructor<?> noArgsCtr = instancClass.getDeclaredConstructor();
                return noArgsCtr.newInstance();
            }
            // primitive values cannot be instantiated via reflection, needs special case here
            if ("int".equals(padlockInstanceType.value().type())) {
                final int ctrArg = Integer.parseInt(padlockInstanceType.value().literal());
                final Constructor<?> intArgCtr = instancClass.getDeclaredConstructor(int.class);
                return intArgCtr.newInstance(ctrArg);
            }
            // instantiate the class instance via reflection
            final Object ctrArgument = this.instantiateInstanceType(padlockInstanceType.value());
            final Constructor<?> classArgCtr =
                    instancClass.getDeclaredConstructor(ctrArgument.getClass().getInterfaces().length > 0 ?
                            ctrArgument.getClass().getInterfaces()[0] : ctrArgument.getClass());
            return classArgCtr.newInstance(ctrArgument);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            Logger logger = Logger.getLogger(PadlockBuilder.class.getName());
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Failed to build Padlock Configuration due to %s".formatted(e.getMessage()));
            }
        }
        return null;
    }
}
