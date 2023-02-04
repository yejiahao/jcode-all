package com.yejh.jcode.base.disruptor;

public class LongEvent {

    private long value;

    public void set(long value) {
        this.value = value;
    }

    public void clear() {
        value = 0L;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}
