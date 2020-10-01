package com.multithread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {
    static enum Result {
        SUCCESS,FAIL,CANCELLED
    };
    static List<MyTask> tasks = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        MyTask task1 = new MyTask("task1", 3, Result.SUCCESS);
        MyTask task2 = new MyTask("task2", 4, Result.SUCCESS);
        MyTask task3 = new MyTask("task3", 1, Result.FAIL);
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        for (MyTask task:tasks) {
            CompletableFuture.supplyAsync(()->task.runTask()).thenAccept((result)->callback(result,task));
        }
        System.in.read();

    }
    private static void callback(Result result,MyTask task){
        //只要有一个线程失败，就把其他所有线程立刻取消(快速失败)
        if (Result.FAIL==result){
            for (MyTask _task:tasks){
                if (_task!=task){
                    if (!_task.cancelling) {
                        _task.cancel();
                    }
                }
            }
        }
    }
    private static class MyTask{
        private String name;
        private int secondTime;
        private Result result;
        volatile boolean cancelling = false;
        volatile boolean cancelled = false;

        //方便测试这里直接指定线程会返回的结果
        public MyTask(String name, int secondTime, Result result) {
            this.name = name;
            this.secondTime = secondTime;
            this.result = result;
        }

        public Result runTask(){
            int interval = 100;
            int total = 0;

            while (true){
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                total+=interval;
                //超时机制
                if (total>secondTime*1000){
                    System.out.println(name +" 线程超时,直接中断");
                    break;
                }
                if (cancelled){
                    System.out.println(name+" 线程被取消");
                    return Result.CANCELLED;
                }
            }
            System.out.println(name+" end!");
            return result;
        }

        /**
         * 优雅的取消线程执行
         */
        public void cancel(){
            if (!cancelled&&!cancelling) {
                //先变成取消中状态，加同步锁，防止多个线程同时cancel
                synchronized (this) {
                    if (cancelled&&cancelling){
                        return;
                    }
                    cancelling = true;
                    System.out.println(name + " cancelling");
                    //这里要处理取消的操作，比如回滚
                    System.out.println(name + " cancelled");
                    cancelled = true;
                }
            }
        }
    }
}
