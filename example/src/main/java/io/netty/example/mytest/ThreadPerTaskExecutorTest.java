package io.netty.example.mytest;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

public class ThreadPerTaskExecutorTest {
    public static void main(String[] args) {
        ThreadPerTaskExecutor executor = new ThreadPerTaskExecutor(new DefaultThreadFactory(ThreadPerTaskExecutorTest.class));

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("task run.......");
            }
        });
    }
}
