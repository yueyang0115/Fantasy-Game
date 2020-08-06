package edu.duke.ece.fantasy.task;

public abstract class DistributeTask {
    private int distributeKey;

    public DistributeTask(int distributeKey) {
        this.distributeKey = distributeKey;
    }

    public int getDistributeKey() {
        return distributeKey;
    }

    public abstract void action();
}
