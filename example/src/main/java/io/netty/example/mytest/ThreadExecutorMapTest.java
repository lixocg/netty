package io.netty.example.mytest;

import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.internal.ThreadExecutorMap;

public class ThreadExecutorMapTest {
    public static void main(String[] args) {
        ImmediateEventExecutor eventExecutor = ImmediateEventExecutor.INSTANCE;


        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("任务运行中.....");
                try {
                    System.out.println(ThreadExecutorMap.currentExecutor() == eventExecutor);

                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        Runnable apply = ThreadExecutorMap.apply(r, eventExecutor);

        eventExecutor.execute(apply);

        System.out.println(ThreadExecutorMap.currentExecutor() == eventExecutor);
    }
}
