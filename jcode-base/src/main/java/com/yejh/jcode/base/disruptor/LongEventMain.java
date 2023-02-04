package com.yejh.jcode.base.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LongEventMain {

    public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        log.info("Event: {}", event);
    }

    public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }

    public static void main(String[] args) throws InterruptedException {
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;

        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);
        Disruptor<LongEvent> disruptor2 = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith(new LongEventHandler()).then(new ClearingEventHandler());
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0L; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            /**
             ringBuffer.publishEvent(LongEventMain::translate, bb);
             ringBuffer.publishEvent((event, sequence) -> event.set(bb.getLong(0)));
             */
            TimeUnit.SECONDS.sleep(1L);
        }
    }
}
