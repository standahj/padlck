package com.cleverthis.interview.domain;

import com.cleverthis.interview.padlock.PadlockImpl;

/**
 * A delegating wrapper to make the PadlockImpl class compatible with the IPadlock interface.
 */
public class PadlockImplDelegate implements IPadlock {

    final private PadlockImpl delegate;

    public PadlockImplDelegate(PadlockImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getNumpadSize() {
        return this.delegate.getNumpadSize();
    }

    @Override
    public Integer writeInputBuffer(int address, int keyIndex) {
        return this.delegate.writeInputBuffer(address, keyIndex);
    }

    @Override
    public boolean isPasscodeCorrect() {
        return this.delegate.isPasscodeCorrect();
    }

    @Override
    public long getWriteCounter() {
        return this.delegate.getWriteCounter();
    }

    @Override
    public long getCheckCounter() {
        return this.delegate.getCheckCounter();
    }

    @Override
    public void resetCounter() {
        this.delegate.resetCounter();
    }
}
