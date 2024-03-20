package com.yejh.jcode.base;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * LRU 实现类
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2024-03-19
 * @since 2.0.1
 */
@Slf4j
public class LRUCase<K, V> {

    private final int CAPACITY; // 容量

    private int size; // 元素个数

    private MyNode<K, V> head;

    public LRUCase(final int CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public V get(K key) {
        return getSet(key, null);
    }

    public V set(@NonNull K key, @NonNull V val) {
        return getSet(key, val);
    }

    private V getSet(K key, V val) {
        MyNode<K, V> prev = null;
        MyNode<K, V> node = head;
        while (Objects.nonNull(node)) {
            if (Objects.equals(node.k, key)) {
                if (Objects.nonNull(val)) {
                    node.v = val;
                }
                // 移动到 head
                if (node != head) {
                    prev.next = node.next;
                    node.next = head;
                    head = node;
                }
                log.info("找到（更新）数据: {}, 列表: {}", node, this.listAll());
                return node.v;
            }
            prev = node;
            node = node.next;
        }
        log.warn("找不到数据: {}, 列表: {}", key, this.listAll());
        return null;
    }

    public void add(K key, V val) {
        head = new MyNode<>(key, val, head);
        if (size++ >= CAPACITY) {
            int times = CAPACITY;
            MyNode<K, V> node = head;
            while (times-- > 1) {
                node = node.next;
            }
            node.next = node.next.next; // node 是倒数第二个元素
            size--;
        }
        log.info("新增数据: {}, 列表: {}", head, this.listAll());
    }

    public String listAll() {
        StringJoiner sj = new StringJoiner(", ");
        MyNode<K, V> node = head;
        while (Objects.nonNull(node)) {
            sj.add(node.toString());
            node = node.next;
        }
        return sj.toString();
    }

    static class MyNode<K, V> {
        K k;
        V v;
        MyNode<K, V> next;

        public MyNode(K k, V v, MyNode<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        @Override
        public String toString() {
            return String.format("(%s -> %s)", this.k.toString(), this.v.toString());
        }
    }

    public static void main(String[] args) {
        LRUCase<Integer, String> obj = new LRUCase<>(5);
        for (int i = 0; i < 4; i++) {
            obj.add(i, i + "");
        }
        obj.get(2);
        obj.get(1);
        obj.get(22);
        obj.set(0, "00");
        obj.add(45, "张三");
        obj.add(67, "李四");
    }
}
