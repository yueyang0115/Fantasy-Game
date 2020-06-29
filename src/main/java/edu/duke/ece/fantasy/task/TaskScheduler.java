package edu.duke.ece.fantasy.task;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskScheduler {
    private PriorityQueue<ScheduledTask> tasksQueue;

    public TaskScheduler() {
        tasksQueue = new PriorityQueue<>(new Comparator<ScheduledTask>() {
            @Override
            public int compare(ScheduledTask scheduledTask1, ScheduledTask scheduledTask2) {
                return Long.compare(scheduledTask1.getWhen(), scheduledTask2.getWhen());
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
        return Long.MAX_VALUE;
    }

    // this method will be called when the first task in queue should be executed
    public void runReadyTasks(){
        // inject metaDAO
        long now = System.currentTimeMillis();
        while(!tasksQueue.isEmpty()){
            ScheduledTask scheduledTask = tasksQueue.peek();
            //if: the task is ready
            if(scheduledTask.getWhen() <= now ){
                scheduledTask.doTask();
                tasksQueue.poll();
                //if the task should be done repeatedly, update the next time to do it, add it back to queue
                if(scheduledTask.isRepeating()){
                    scheduledTask.updateWhen();
                    tasksQueue.offer(scheduledTask);
                }
            }
            // else: all tasks should be executed in the future
            else{
                break;
            }
        }
    }

    public void addTask(ScheduledTask scheduledTask){
        this.tasksQueue.offer(scheduledTask);
    }

}
