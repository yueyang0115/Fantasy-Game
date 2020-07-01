package edu.duke.ece.fantasy.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskSchedulerTest {
    @Test
    public void testAll(){
        unrepeatedTaskTest();
        repeatedTaskTest();
    }

    public void repeatedTaskTest(){
        TaskScheduler taskScheduler = new TaskScheduler();
        long now = System.currentTimeMillis();
        ScheduledTask task = new ScheduledTask(now,1000,true) {
            @Override
            public void doTask() {
            }
        };
        taskScheduler.addTask(task);
        if (taskScheduler.getTimeToNextTask() <= 0) {
            taskScheduler.runReadyTasks();
        }
        assertNotEquals(taskScheduler.getTimeToNextTask(),Long.MAX_VALUE);
    }

    public void unrepeatedTaskTest(){
        TaskScheduler taskScheduler = new TaskScheduler();
        long now = System.currentTimeMillis();
        ScheduledTask task1 = new ScheduledTask(now-2,0,false) {
            @Override
            public void doTask() {
            }
        };
        ScheduledTask task2 = new ScheduledTask(now-1,0,false) {
            @Override
            public void doTask() {
            }
        };
        taskScheduler.addTask(task2);
        taskScheduler.addTask(task1);
        assertEquals(taskScheduler.getTimeToNextTask(),now-2-System.currentTimeMillis());
        taskScheduler.runReadyTasks();
        assertEquals(taskScheduler.getTimeToNextTask(),Long.MAX_VALUE);
    }
}
