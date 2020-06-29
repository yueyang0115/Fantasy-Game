package edu.duke.ece.fantasy.task;

public abstract class ScheduledTask implements Task{
    private long when; // the time when the task should be executed
    private int repeatedInterval;
    private boolean repeating;

    public ScheduledTask(long when, int repeatedInterval, boolean repeating) {
        this.when = when;
        this.repeatedInterval = repeatedInterval;
        this.repeating = repeating;
    }

    public int getRepeatedInterval() { return repeatedInterval; }

    public void setRepeatedInterval(int repeatedInterval) { this.repeatedInterval = repeatedInterval; }

    public long getWhen() { return when; }

    public void setWhen(long when) { this.when = when; }

    public void updateWhen(){ this.when += repeatedInterval; }

    public boolean isRepeating() { return repeating; }

}
