package com.example.ljh.faceln;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ljh on 2017/11/28.
 */

public class ThreadManager {
    static ExecutorService executorService;
    static Runnable runnable;
    public static ExecutorService startThread(){
        executorService = Executors.newCachedThreadPool();
        return executorService;
    }
}
