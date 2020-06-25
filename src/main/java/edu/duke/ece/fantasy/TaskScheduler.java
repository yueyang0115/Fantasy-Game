package edu.duke.ece.fantasy;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskScheduler {
    private PriorityQueue<Task> tasksQueue;

    public TaskScheduler() {
        tasksQueue = new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return Long.compare(task1.getWhen(),task2.getWhen());
            }
        });
    }

    //how much time left before the next task to be executed
    public long getTimeToNextTask(){
        if(!tasksQueue.isEmpty()){
            long now = System.currentTimeMillis();
//            System.out.println("in getTimeToNextTask, now is "+now);
//            System.out.println("in getTimeToNextTask, tasksQueue.peek().getWhen() is "+tasksQueue.peek().getWhen());
            return tasksQueue.peek().getWhen() - now;
        }
        return Integer.MAX_VALUE;
    }

    // this method will be called when the first task in queue should be executed
    public void runReadyTasks(){
        long now = System.currentTimeMillis();
        while(!tasksQueue.isEmpty()){
            Task task = tasksQueue.peek();
            //if: the task is ready
            if(task.getWhen() <= now ){
                task.doTask();
                tasksQueue.poll();
                //if the task should be done repeatedly, update the next time to do it, add it back to queue
                if(task.isRepeating()){
                    task.updateWhen();
                    tasksQueue.offer(task);
                }
            }
            // else: all tasks should be executed in the future
            else{
                break;
            }
        }
    }

    public void addTask(Task task){
        this.tasksQueue.offer(task);
    }

}
