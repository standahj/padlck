package com.cleverthis.interview.domain;

import com.cleverthis.interview.configuration.PadlockAccessType;
import com.cleverthis.interview.configuration.PadlockConfiguration;
import com.cleverthis.interview.padlock.PadlockImpl;

import java.lang.reflect.Constructor;
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

    public PadlockBuilder withPadlockConfiguration(PadlockConfiguration padlockConfiguration) {
        this.padlockConfiguration = padlockConfiguration;
        return this;
    }

    public PadlockBuilder withKeypadSize(int keypadSize) {
        this.keypadSize = Integer.valueOf(keypadSize);
        return this;
    }

    /**
     * Builder - build the PadlockAccessor instance based on the provided configuration or keypadSize.
     * If the configuration is missing, it will use Java padlock type and default keypad size, unless desired keypadSize was provided.
     *
     * @return the PadlockAccessor instance.
     */
    public IPadlock build() {
        final PadlockAccessType _accessType =
                this.padlockConfiguration != null ? this.padlockConfiguration.accessType() : PadlockConfiguration.PADLOCK_DEFAULT_ACCESS_TYPE;
        final int _keypadSize =
                this.padlockConfiguration != null ? this.padlockConfiguration.padlockKeypadSize() :
                        this.keypadSize != null ? this.keypadSize : PadlockConfiguration.PADLOCK_DEFAULT_KEYPAD_SIZE;
        switch (_accessType) {
            case CUSTOM: {
                IPadlock accessor = this.buildCustom(this.padlockConfiguration);
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
     * Build the PadlockAccessor for CUSTOM configuration.
     *
     * @param configuration the Padlock configuration
     * @return PadlockAccessor instance or null if not possible to build one.
     */
    private IPadlock buildCustom(PadlockConfiguration configuration) {
        if (padlockConfiguration.instanceType() != null) {
            try {
                return (IPadlock) instantiateInstanceType(padlockConfiguration.instanceType());
            } catch (Exception cfgException) {
                Logger.getLogger(PadlockBuilder.class.getName()).log(Level.SEVERE, "Failed to build Padlock accessor", cfgException);
            }
        }
        // if something went sideways....
        return null;
    }

    /**
     * Build the PadlockAccessor class from the chain of constructor declarations
     * This uses simplified logic to showcase how ths can be done
     * There are no edge case checks here, as this methods should throw exception if it is not possible to build the PadlockAccessor.
     * The method is called recursively if the constructor argument is a class instance.
     *
     * @param padlockInstanceType  configuration - chain of constructors
     * @return The instantiated class for the given invocation. Top invocation shall be the instance of PadlockAccessor class.
     * @throws Exception if it is not possible to build the accessor from the configuration.
     */
    private Object instantiateInstanceType(PadlockConfiguration.PadlockInstanceType padlockInstanceType) throws Exception {
        final Class<?> instancClass = Class.forName(padlockInstanceType.type());
        // this is for possible future cases, current implementation has no no-args constructors for possible candidates
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
    }
}
