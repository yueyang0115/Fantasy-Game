package edu.duke.ece.fantasy;

import org.checkerframework.common.reflection.qual.GetClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum TaskHandler {
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(TaskHandler.class);
    private final int CORE_SIZE = Runtime.getRuntime().availableProcessors();

    ExecutorService workerPool = Executors.newFixedThreadPool(CORE_SIZE);
    Boolean isRun; // make sure taskWorker accept task all the time

    public void initialize(){
        isRun = true;
        for (int i=0;i<CORE_SIZE;i++){
            workerPool.submit(new TaskWorker(i));
        }
    }

    public void shutDown(){
        isRun = false;
        workerPool.shutdown();
    }

    private class TaskWorker implements Runnable{
        private int id;

        public TaskWorker(int index){
            id = index;
        }

        public void addTask(){

        }

        @Override
        public void run() {
            while(isRun){

            }
        }
    }
}
