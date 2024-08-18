package com.cleverthis.interview.domain;

/**
 * Methods identified from PadlockImpl that represent a generic padlock type.
 */
public interface IPadlock {

    int getNumpadSize();

    Integer writeInputBuffer(int address, int keyIndex);

    boolean isPasscodeCorrect();

    long getWriteCounter();

    long getCheckCounter();

    void resetCounter();
}
