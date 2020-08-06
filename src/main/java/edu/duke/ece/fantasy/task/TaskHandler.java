package edu.duke.ece.fantasy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public enum TaskHandler {
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(TaskHandler.class);
    private final int CORE_SIZE = Runtime.getRuntime().availableProcessors();

    List<TaskWorker> workerPool = new ArrayList<>();
    Boolean isRun; // make sure taskWorker accept task all the time

    public void initialize() {
        isRun = true;
        for (int i = 0; i < CORE_SIZE; i++) {
            TaskWorker worker = new TaskWorker(i);
            workerPool.add(worker);
            new Thread(worker).start();
        }
    }

    public void addTask(DistributeTask task) {
        int distributeKey = task.getDistributeKey() % workerPool.size();
        workerPool.get(distributeKey).addTask(task);
    }

    public void shutDown() {
        isRun = false;
    }

    private class TaskWorker implements Runnable {
        private int id;
        BlockingDeque<DistributeTask> taskQueue = new LinkedBlockingDeque<>();

        public TaskWorker(int index) {
            id = index;
        }

        public void addTask(DistributeTask task) {
            taskQueue.add(task);
        }

        @Override
        public void run() {
            while (isRun) {
                try {
                    DistributeTask task = taskQueue.take();
                    task.action();
                } catch (InterruptedException e) {
                    logger.error("task worker " + id, e);
                }
            }
        }
    }
}
