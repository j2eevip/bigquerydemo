package com.github.sy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * @author Sherlock
 * @since 2021/10/25-15:05
 */
public class Test {

    public static void newThread(final Lock lock1, final Lock lock2, final Function<Integer, Boolean> func) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (func.apply(i) && lock2.tryLock()) {
                    lock1.lock();
                    try {
                        System.out.println(i);
                    } finally {
                        lock1.unlock();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        newThread(lock1, lock2, x -> x % 2 == 0);
        newThread(lock2, lock1, x -> x % 2 != 0);
    }
}
