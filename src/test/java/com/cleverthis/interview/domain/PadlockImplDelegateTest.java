package com.cleverthis.interview.domain;

import com.cleverthis.interview.padlock.PadlockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadlockImplDelegateTest {

    private PadlockImplDelegate delegate;
    private int keypadSize = 7;

    @BeforeEach
    void setUp() {
        this.keypadSize = Double.valueOf(Math.random() * 20).intValue() + 5;
        this.delegate = new PadlockImplDelegate(new PadlockImpl(this.keypadSize));
    }

    @Test
    void getNumpadSize() {
        assertEquals(this.keypadSize, this.delegate.getNumpadSize());
    }

    @Test
    void writeInputBuffer() {
        // given
        this.delegate.writeInputBuffer(0, 0);
        this.delegate.writeInputBuffer(1, 1);
        this.delegate.writeInputBuffer(2, 2);
        // then
        assertEquals(3, this.delegate.getWriteCounter());
        assertEquals(0, this.delegate.getCheckCounter());
    }

    @Test
    void isPasscodeCorrect() {
        // given
        for (int i = 0; i < this.keypadSize; i++) {
            this.delegate.writeInputBuffer(i, i);
        }
        // when
        boolean test = this.delegate.isPasscodeCorrect();
        // then
        assertEquals(this.keypadSize, this.delegate.getWriteCounter());
        assertEquals(1, this.delegate.getCheckCounter());
    }

    @Test
    void getWriteCounter() {
        // given
        for (int i = 0; i < this.keypadSize; i++) {
            this.delegate.writeInputBuffer(i, i);
        }
        // then
        assertEquals(this.keypadSize, this.delegate.getWriteCounter());
    }

    @Test
    void getCheckCounter() {
        // given
        for (int i = 0; i < this.keypadSize; i++) {
            this.delegate.writeInputBuffer(i, i);
        }
        // when
        boolean test = this.delegate.isPasscodeCorrect();
        // then
        assertEquals(1, this.delegate.getCheckCounter());
    }

    @Test
    void resetCounter() {
        // given
        for (int i = 0; i < this.keypadSize; i++) {
            this.delegate.writeInputBuffer(i, i);
        }
        // when
        boolean test = this.delegate.isPasscodeCorrect();
        this.delegate.resetCounter();
        // then
        assertEquals(0, this.delegate.getWriteCounter());
        assertEquals(0, this.delegate.getCheckCounter());
    }
}