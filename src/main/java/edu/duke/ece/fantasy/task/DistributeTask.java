package edu.duke.ece.fantasy.task;

public abstract class DistributeTask {
    private int distributeKey;

    public DistributeTask() {
    }

    public DistributeTask(int distributeKey) {
        this.distributeKey = distributeKey;
    }

    public int getDistributeKey() {
        return distributeKey;
    }

    public void setDistributeKey(int distributeKey) {
        this.distributeKey = distributeKey;
    }

    public abstract void action();
}
