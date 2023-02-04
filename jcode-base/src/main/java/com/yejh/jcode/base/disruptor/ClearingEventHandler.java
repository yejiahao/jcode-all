package com.yejh.jcode.base.disruptor;

import com.lmax.disruptor.EventHandler;

public class ClearingEventHandler/*<T>*/ implements EventHandler<LongEvent/*ObjectEvent<T>*/> {

    @Override
    public void onEvent(LongEvent/*ObjectEvent<T>*/ event, long sequence, boolean endOfBatch) {
        event.clear();
    }
}

class ObjectEvent<T> {

    T val;

    void clear() {
        val = null;
    }
}
